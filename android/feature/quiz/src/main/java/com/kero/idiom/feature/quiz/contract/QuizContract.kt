package com.kero.idiom.feature.quiz.contract

import com.kero.idiom.domain.model.Idiom // 추후 생성

data class QuizState(
    val isLoading: Boolean = false,
    val currentIdiom: Idiom? = null,
    val score: Int = 0,
    val isAnswerCorrect: Boolean? = null
)

sealed interface QuizIntent {
    data object LoadNextQuiz : QuizIntent
    data class SubmitAnswer(val answer: String) : QuizIntent
}

sealed interface QuizSideEffect {
    data class ShowToast(val message: String) : QuizSideEffect
    data class NavigateToResult(val finalScore: Int) : QuizSideEffect
}
