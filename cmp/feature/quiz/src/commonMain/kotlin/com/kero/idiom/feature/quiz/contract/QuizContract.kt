package com.kero.idiom.feature.quiz.contract

import com.kero.idiom.domain.model.Quiz

data class QuizState(
    val isLoading: Boolean = false,
    val currentQuiz: Quiz? = null,
    val selectedOption: String? = null,
    val inputText: String = "", // 주관식 입력값
    val isCorrect: Boolean? = null,
    val score: Int = 0,
    val quizCount: Int = 0,
    val isHintRevealed: Boolean = false
)

sealed interface QuizIntent {
    data class SelectOption(val option: String) : QuizIntent
    data class InputAnswer(val input: String) : QuizIntent // 실시간 입력 업데이트
    data object SubmitAnswer : QuizIntent // 주관식 정답 제출
    data object NextQuiz : QuizIntent
    data object ShowHint : QuizIntent
}

sealed interface QuizSideEffect {
    data class NavigateToResult(val score: Int, val total: Int) : QuizSideEffect
    data object ShowCorrectEffect : QuizSideEffect
    data object ShowWrongEffect : QuizSideEffect
}
