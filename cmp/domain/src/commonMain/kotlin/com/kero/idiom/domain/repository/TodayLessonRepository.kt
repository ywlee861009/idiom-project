package com.kero.idiom.domain.repository

import com.kero.idiom.domain.model.TodayLesson

/** Day arguments are epoch days of local calendar dates. */
interface TodayLessonRepository {
    suspend fun get(day: Long): TodayLesson?
    suspend fun previewTheme(day: Long): String
    suspend fun start(day: Long): TodayLesson
    suspend fun revealHint(day: Long, questionIndex: Int): TodayLesson
    suspend fun submitAnswer(day: Long, questionIndex: Int, choice: String, completionDay: Long = day): TodayLesson
    suspend fun advance(day: Long, questionIndex: Int): TodayLesson
    suspend fun countCompletedDaysThisWeek(day: Long): Int
    suspend fun isCompleted(day: Long): Boolean
    suspend fun isFirstCompletion(day: Long): Boolean
}
