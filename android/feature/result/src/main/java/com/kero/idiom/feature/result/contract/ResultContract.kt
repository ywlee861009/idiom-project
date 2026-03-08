package com.kero.idiom.feature.result.contract

data class ResultState(
    val finalScore: Int = 0
)

sealed interface ResultIntent {
    data object RestartQuiz : ResultIntent
}

sealed interface ResultSideEffect {
    data object NavigateToQuiz : ResultSideEffect
}
