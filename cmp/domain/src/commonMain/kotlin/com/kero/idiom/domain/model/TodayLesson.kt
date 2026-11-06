package com.kero.idiom.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TodayLesson(
    val day: Long,
    val theme: String,
    val questions: List<TodayLessonQuestion>,
    val currentIndex: Int = 0,
    val newlyCollected: List<Idiom> = emptyList()
) {
    val isCompleted: Boolean get() = questions.all { it.selectedAnswer != null }
    val isResultVisible: Boolean get() = currentIndex == questions.size
    val correctCount: Int get() = questions.count { it.isCorrect == true }
}

@Serializable
data class TodayLessonQuestion(
    val idiom: Idiom,
    val prompt: String,
    val alternatePrompt: String,
    val options: List<String>,
    val hint: String,
    val alternateHint: String = hint,
    val isReview: Boolean = false,
    val isHintRevealed: Boolean = false,
    val selectedAnswer: String? = null
) {
    val isCorrect: Boolean? get() = selectedAnswer?.let { it == idiom.word }
}
