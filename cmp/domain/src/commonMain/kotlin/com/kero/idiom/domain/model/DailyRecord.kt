package com.kero.idiom.domain.model

data class DailyRecord(
    val date: String,
    val solvedCount: Int,
    val correctCount: Int,
    val earnedXp: Int
) {
    val accuracy: Float
        get() = if (solvedCount > 0) correctCount.toFloat() / solvedCount else 0f
}
