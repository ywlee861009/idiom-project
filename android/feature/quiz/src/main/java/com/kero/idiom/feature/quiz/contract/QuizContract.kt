package com.kero.idiom.feature.quiz.contract

import com.kero.idiom.domain.model.Quiz

data class QuizState(
    val isLoading: Boolean = false,
    val quiz: Quiz? = null,
    val score: Int = 0,
    val isAnswerCorrect: Boolean? = null
)

sealed interface QuizIntent {
    data object LoadNextQuiz : QuizIntent
    data class SubmitAnswer(val answer: String) : QuizIntent // String으로 변경
}

sealed interface QuizSideEffect {
    data class ShowToast(val message: String) : QuizSideEffect
    data class NavigateToResult(val finalScore: Int) : QuizSideEffect
}
