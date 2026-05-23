package com.kero.idiom.feature.quiz.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kero.idiom.core.ads.AdController
import com.kero.idiom.domain.usecase.GetRandomQuizUseCase
import com.kero.idiom.domain.usecase.GetUserStatsUseCase
import com.kero.idiom.domain.usecase.RecordCorrectAnswerUseCase
import com.kero.idiom.domain.usecase.UpdateUserStatsUseCase
import com.kero.idiom.feature.quiz.contract.QuizIntent
import com.kero.idiom.feature.quiz.contract.QuizSideEffect
import com.kero.idiom.feature.quiz.contract.QuizState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TOTAL_QUIZ_COUNT = 5

class QuizViewModel(
    private val getRandomQuizUseCase: GetRandomQuizUseCase,
    private val recordCorrectAnswerUseCase: RecordCorrectAnswerUseCase,
    private val updateUserStatsUseCase: UpdateUserStatsUseCase,
    private val getUserStatsUseCase: GetUserStatsUseCase,
    private val adController: AdController
) : ViewModel() {

    private val _state = MutableStateFlow(QuizState())
    val state: StateFlow<QuizState> = _state.asStateFlow()

    private val _sideEffect = Channel<QuizSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        viewModelScope.launch {
            // 전역 콤보를 불러와 세션 시작 콤보로 설정
            val globalCombo = getUserStatsUseCase().first().globalCombo
            _state.update { it.copy(comboCount = globalCombo) }
        }
        loadNextQuiz()
        adController.loadInterstitial() // 전면 광고 미리 로드
        adController.loadRewardedAd()   // 리워드 광고 미리 로드
    }

    fun handleIntent(intent: QuizIntent) {
        when (intent) {
            is QuizIntent.SelectOption -> checkAnswer(intent.option)
            is QuizIntent.InputAnswer -> _state.update { it.copy(inputText = intent.input) }
            QuizIntent.SubmitAnswer -> checkAnswer(state.value.inputText)
            QuizIntent.NextQuiz -> onNextQuiz()
            is QuizIntent.SelectPoolLetter -> onSelectPoolLetter(intent.index)
            is QuizIntent.UndoSelectedLetter -> onUndoSelectedLetter(intent.position)
            QuizIntent.ShowHint -> {
                val isShown = adController.showRewardedAd {
                    _state.update { it.copy(isHintRevealed = true) }
                }
                if (!isShown) {
                    viewModelScope.launch {
                        _sideEffect.send(QuizSideEffect.ShowToast("광고를 준비 중입니다. 잠시 후 다시 눌러주세요."))
                    }
                }
            }
        }
    }

    private fun onNextQuiz() {
        if (state.value.quizCount >= TOTAL_QUIZ_COUNT) {
            viewModelScope.launch {
                // 통계 업데이트
                updateUserStatsUseCase(state.value.score, TOTAL_QUIZ_COUNT, state.value.totalXpGained, state.value.comboCount)

                _sideEffect.send(QuizSideEffect.NavigateToResult(state.value.score, TOTAL_QUIZ_COUNT, state.value.totalXpGained))
            }
        } else {
            loadNextQuiz()
        }
    }

    private fun loadNextQuiz() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val quiz = getRandomQuizUseCase()
            _state.update {
                it.copy(
                    isLoading = false,
                    currentQuiz = quiz,
                    selectedOption = null,
                    inputText = "", // 입력값 초기화
                    isCorrect = null,
                    isHintRevealed = false,
                    quizCount = it.quizCount + 1,
                    selectedLetters = emptyList(),
                    usedPoolIndices = emptySet()
                )
            }
        }
    }

    private fun onSelectPoolLetter(poolIndex: Int) {
        if (state.value.isCorrect != null) return
        if (poolIndex in state.value.usedPoolIndices) return

        val quiz = state.value.currentQuiz ?: return
        val charPool = quiz.charPool
        if (poolIndex !in charPool.indices) return

        val newSelected = state.value.selectedLetters + charPool[poolIndex]
        val newUsed = state.value.usedPoolIndices + poolIndex

        _state.update { it.copy(selectedLetters = newSelected, usedPoolIndices = newUsed) }

        // 4글자가 채워지면 자동으로 정답 판정
        if (newSelected.size == quiz.originalIdiom.word.length) {
            checkAnswer(newSelected.joinToString(""))
        }
    }

    private fun onUndoSelectedLetter(position: Int) {
        if (state.value.isCorrect != null) return

        val quiz = state.value.currentQuiz ?: return
        val selected = state.value.selectedLetters
        if (position !in selected.indices) return

        // 해당 위치의 글자에 대응하는 풀 인덱스를 찾아서 되돌리기
        val letterToUndo = selected[position]
        val usedIndices = state.value.usedPoolIndices.toMutableSet()

        // usedPoolIndices에서 position 순서에 맞는 인덱스를 제거
        val sortedUsed = usedIndices.sorted()
        if (position < sortedUsed.size) {
            usedIndices.remove(sortedUsed[position])
        }

        val newSelected = selected.toMutableList().apply { removeAt(position) }
        _state.update { it.copy(selectedLetters = newSelected, usedPoolIndices = usedIndices) }
    }

    // 연속 정답 XP 보너스: combo 2→+1, 3→+2, 4→+3, 5→+4, 6+→+5 (최대)
    private fun comboXpBonus(combo: Int): Int = (combo - 1).coerceIn(0, 5)

    private fun checkAnswer(option: String) {
        // 이미 답변을 선택했거나 정답 처리가 완료된 경우 무시
        if (state.value.isCorrect != null) return
        
        val currentQuiz = state.value.currentQuiz ?: return
        val trimmedInput = option.trim()
        
        // 정답 판정 로직 개선
        val isCorrect = if (currentQuiz.options.isEmpty() && trimmedInput.length == currentQuiz.blankIndices.size) {
            // 빈칸 글자수만 입력한 경우 (예: "건_일_" + "곤척")
            val reconstructed = currentQuiz.originalIdiom.word.mapIndexed { index, c ->
                if (index in currentQuiz.blankIndices) {
                    trimmedInput[currentQuiz.blankIndices.indexOf(index)]
                } else {
                    c
                }
            }.joinToString("")
            reconstructed == currentQuiz.answer
        } else {
            // 전체 4글자를 입력하거나 객관식인 경우
            trimmedInput == currentQuiz.answer
        }
        
        viewModelScope.launch {
            if (isCorrect) {
                val newComboCount = state.value.comboCount + 1
                _sideEffect.send(QuizSideEffect.ShowCorrectEffect)
                if (newComboCount >= 2) {
                    _sideEffect.send(QuizSideEffect.ShowComboEffect(newComboCount))
                }
                recordCorrectAnswerUseCase(currentQuiz.originalIdiom.word)
                
                _state.update {
                    it.copy(
                        selectedOption = if (currentQuiz.options.isNotEmpty()) option else null,
                        inputText = if (currentQuiz.options.isEmpty()) option else it.inputText,
                        isCorrect = true,
                        score = it.score + 1,
                        comboCount = newComboCount,
                        totalXpGained = it.totalXpGained + currentQuiz.originalIdiom.level + comboXpBonus(newComboCount)
                    )
                }
            } else {
                _sideEffect.send(QuizSideEffect.ShowWrongEffect)
                _state.update {
                    it.copy(
                        selectedOption = if (currentQuiz.options.isNotEmpty()) option else null,
                        inputText = if (currentQuiz.options.isEmpty()) option else it.inputText,
                        isCorrect = false,
                        comboCount = 0 // 오답 시 콤보 초기화
                    )
                }
            }
        }
    }
}
