package com.kero.idiom.feature.quiz.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kero.idiom.domain.usecase.GetRandomQuizUseCase
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

class QuizViewModel(
    private val getRandomQuizUseCase: GetRandomQuizUseCase
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
            QuizIntent.NextQuiz -> loadNextQuiz()
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
        val newCount = state.value.quizCount + 1
        
        _state.update { 
            it.copy(
                selectedOption = option, 
                isCorrect = isCorrect,
                score = newScore,
                quizCount = newCount
            ) 
        }

        viewModelScope.launch {
            if (newCount >= 10) {
                // 10문제가 모두 끝나면 결과 화면으로 이동
                _sideEffect.send(QuizSideEffect.NavigateToResult(newScore, 10))
            } else {
                // 10문제가 안 끝났으면 정답/오답 효과 보여주기
                if (isCorrect) {
                    _sideEffect.send(QuizSideEffect.ShowCorrectEffect)
                } else {
                    _sideEffect.send(QuizSideEffect.ShowWrongEffect)
                }
            }
        }
    }
}
