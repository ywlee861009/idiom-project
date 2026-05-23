package com.kero.idiom.feature.quiz.contract

import com.kero.idiom.domain.model.Quiz

data class QuizState(
    val isLoading: Boolean = false,
    val currentQuiz: Quiz? = null,
    val selectedOption: String? = null,
    val inputText: String = "", // 주관식 입력값
    val isCorrect: Boolean? = null,
    val score: Int = 0,
    val totalXpGained: Int = 0,
    val comboCount: Int = 0, // 연속 정답 횟수 추가
    val quizCount: Int = 0,
    val isHintRevealed: Boolean = false,
    val selectedLetters: List<String> = emptyList(), // 순서 맞히기: 선택된 글자들
    val usedPoolIndices: Set<Int> = emptySet() // 순서 맞히기: 사용된 풀 인덱스
)

sealed interface QuizIntent {
    data class SelectOption(val option: String) : QuizIntent
    data class InputAnswer(val input: String) : QuizIntent // 실시간 입력 업데이트
    data object SubmitAnswer : QuizIntent // 주관식 정답 제출
    data object NextQuiz : QuizIntent
    data object ShowHint : QuizIntent
    data class SelectPoolLetter(val index: Int) : QuizIntent // 순서 맞히기: 글자 풀에서 선택
    data class UndoSelectedLetter(val position: Int) : QuizIntent // 순서 맞히기: 선택한 글자 되돌리기
}
sealed interface QuizSideEffect {
    data class NavigateToResult(val score: Int, val total: Int, val xpGained: Int) : QuizSideEffect
    data object ShowCorrectEffect : QuizSideEffect
    data object ShowWrongEffect : QuizSideEffect
    data class ShowComboEffect(val comboCount: Int) : QuizSideEffect // 콤보 효과 추가
    data class ShowToast(val message: String) : QuizSideEffect
}
