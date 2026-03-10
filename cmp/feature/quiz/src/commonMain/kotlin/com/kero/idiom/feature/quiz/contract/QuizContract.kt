package com.kero.idiom.feature.quiz.contract

import com.kero.idiom.domain.model.Quiz

data class QuizState(
    val isLoading: Boolean = false,
    val currentQuiz: Quiz? = null,
    val selectedOption: String? = null,
    val isCorrect: Boolean? = null,
    val score: Int = 0,
    val quizCount: Int = 0
)

sealed interface QuizIntent {
    data class SelectOption(val option: String) : QuizIntent
    data object NextQuiz : QuizIntent
}

sealed interface QuizSideEffect {
    data class NavigateToResult(val score: Int, val total: Int) : QuizSideEffect
    data object ShowCorrectEffect : QuizSideEffect
    data object ShowWrongEffect : QuizSideEffect
}
