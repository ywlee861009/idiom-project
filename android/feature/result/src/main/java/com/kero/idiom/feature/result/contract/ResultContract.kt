package com.kero.idiom.feature.result.contract

data class ResultState(
    val score: Int = 0,
    val isLoading: Boolean = false,
    val isNewHighScore: Boolean = false
)

sealed interface ResultIntent {
    data class Init(val score: Int) : ResultIntent
    data object OnRetryClick : ResultIntent
    data object OnHomeClick : ResultIntent
}

sealed interface ResultSideEffect {
    data object NavigateToQuiz : ResultSideEffect
    data object NavigateToHome : ResultSideEffect
}
