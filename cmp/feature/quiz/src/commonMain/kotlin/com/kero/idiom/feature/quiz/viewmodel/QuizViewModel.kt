package com.kero.idiom.feature.quiz.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kero.idiom.domain.usecase.GetRandomQuizUseCase
import com.kero.idiom.domain.usecase.RecordCorrectAnswerUseCase
import com.kero.idiom.domain.usecase.UpdateUserStatsUseCase
import com.kero.idiom.feature.quiz.contract.QuizIntent
import com.kero.idiom.feature.quiz.contract.QuizSideEffect
import com.kero.idiom.feature.quiz.contract.QuizState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TOTAL_QUIZ_COUNT = 5

class QuizViewModel(
    private val getRandomQuizUseCase: GetRandomQuizUseCase,
    private val recordCorrectAnswerUseCase: RecordCorrectAnswerUseCase,
    private val updateUserStatsUseCase: UpdateUserStatsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(QuizState())
    val state: StateFlow<QuizState> = _state.asStateFlow()

    private val _sideEffect = Channel<QuizSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        loadNextQuiz()
    }

    fun handleIntent(intent: QuizIntent) {
        when (intent) {
            is QuizIntent.SelectOption -> checkAnswer(intent.option)
            QuizIntent.NextQuiz -> onNextQuiz()
        }
    }

    private fun onNextQuiz() {
        if (state.value.quizCount >= TOTAL_QUIZ_COUNT) {
            viewModelScope.launch {
                // 통계 업데이트
                updateUserStatsUseCase(state.value.score, TOTAL_QUIZ_COUNT)
                
                _sideEffect.send(QuizSideEffect.NavigateToResult(state.value.score, TOTAL_QUIZ_COUNT))
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
                    isCorrect = null
                )
            }
        }
    }

    private fun checkAnswer(option: String) {
        val currentQuiz = state.value.currentQuiz ?: return
        val isCorrect = option == currentQuiz.answer
        val newScore = if (isCorrect) state.value.score + 1 else state.value.score
        val newCount = state.value.quizCount

        viewModelScope.launch {
            if (isCorrect) {
                _sideEffect.send(QuizSideEffect.ShowCorrectEffect)
                recordCorrectAnswerUseCase(currentQuiz.originalIdiom.word)
            } else {
                _sideEffect.send(QuizSideEffect.ShowWrongEffect)
            }
        }

        _state.update {
            it.copy(
                selectedOption = option,
                isCorrect = isCorrect,
                score = newScore,
                quizCount = newCount + 1
            )
        }
    }
}
