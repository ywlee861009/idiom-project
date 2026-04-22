package com.kero.idiom.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals

class DailyRecordTest {

    @Test
    fun accuracy_returnsCorrectRatio() {
        val record = DailyRecord(date = "2026-04-22", solvedCount = 10, correctCount = 7, earnedXp = 20)
        assertEquals(0.7f, record.accuracy)
    }

    @Test
    fun accuracy_whenAllCorrect_returnsOne() {
        val record = DailyRecord(date = "2026-04-22", solvedCount = 5, correctCount = 5, earnedXp = 15)
        assertEquals(1.0f, record.accuracy)
    }

    @Test
    fun accuracy_whenNoneCorrect_returnsZero() {
        val record = DailyRecord(date = "2026-04-22", solvedCount = 5, correctCount = 0, earnedXp = 0)
        assertEquals(0.0f, record.accuracy)
    }

    @Test
    fun accuracy_whenNoSolved_returnsZero() {
        val record = DailyRecord(date = "2026-04-22", solvedCount = 0, correctCount = 0, earnedXp = 0)
        assertEquals(0.0f, record.accuracy)
    }

    @Test
    fun defaultValues_areCorrect() {
        val record = DailyRecord(date = "2026-04-22", solvedCount = 0, correctCount = 0, earnedXp = 0)
        assertEquals("2026-04-22", record.date)
        assertEquals(0, record.solvedCount)
        assertEquals(0, record.correctCount)
        assertEquals(0, record.earnedXp)
    }
}
