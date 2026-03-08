package com.kero.idiom.feature.quiz.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kero.idiom.domain.usecase.GetRandomQuizUseCase
import com.kero.idiom.feature.quiz.contract.QuizIntent
import com.kero.idiom.feature.quiz.contract.QuizSideEffect
import com.kero.idiom.feature.quiz.contract.QuizState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val getRandomQuizUseCase: GetRandomQuizUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(QuizState())
    val state = _state.asStateFlow()

    private val _effect = Channel<QuizSideEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        loadNextQuiz()
    }

    fun processIntent(intent: QuizIntent) {
        when (intent) {
            is QuizIntent.LoadNextQuiz -> loadNextQuiz()
            is QuizIntent.SubmitAnswer -> checkAnswer(intent.answer)
        }
    }

    private fun loadNextQuiz() {
        if (_state.value.currentQuizIndex >= _state.value.maxQuizzes) {
            viewModelScope.launch {
                _effect.send(QuizSideEffect.NavigateToResult(_state.value.score))
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, isAnswerCorrect = null) }
            val quiz = getRandomQuizUseCase()
            _state.update {
                it.copy(
                    isLoading = false,
                    quiz = quiz,
                    currentQuizIndex = it.currentQuizIndex + 1
                )
            }
        }
    }

    private fun checkAnswer(answer: String) {
        val currentQuiz = _state.value.quiz ?: return
        val isCorrect = currentQuiz.answer == answer

        if (isCorrect) {
            _state.update { 
                it.copy(
                    score = it.score + 10,
                    isAnswerCorrect = true 
                ) 
            }
            viewModelScope.launch {
                _effect.send(QuizSideEffect.ShowToast("정답입니다!"))
                delay(1000)
                loadNextQuiz()
            }
        } else {
            _state.update { it.copy(isAnswerCorrect = false) }
            viewModelScope.launch {
                _effect.send(QuizSideEffect.ShowToast("오답입니다! 정답: ${currentQuiz.answer}"))
                delay(1500)
                loadNextQuiz() 
            }
        }
    }
}
