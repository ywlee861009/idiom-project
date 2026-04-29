package com.kero.idiom.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals

/** 일일 학습 기록(DailyRecord) 모델의 정확도 계산 및 기본값 검증 */
class DailyRecordTest {

    /** 정확도가 정답수/풀이수 비율로 올바르게 계산되는지 검증 (7/10 = 0.7) */
    @Test
    fun accuracy_returnsCorrectRatio() {
        val record = DailyRecord(date = "2026-04-22", solvedCount = 10, correctCount = 7, earnedXp = 20)
        assertEquals(0.7f, record.accuracy)
    }

    /** 전문 정답일 때 정확도가 1.0(100%)인지 검증 */
    @Test
    fun accuracy_whenAllCorrect_returnsOne() {
        val record = DailyRecord(date = "2026-04-22", solvedCount = 5, correctCount = 5, earnedXp = 15)
        assertEquals(1.0f, record.accuracy)
    }

    /** 전문 오답일 때 정확도가 0.0(0%)인지 검증 */
    @Test
    fun accuracy_whenNoneCorrect_returnsZero() {
        val record = DailyRecord(date = "2026-04-22", solvedCount = 5, correctCount = 0, earnedXp = 0)
        assertEquals(0.0f, record.accuracy)
    }

    /** 풀이 수가 0일 때 0으로 나누기 오류 없이 정확도 0.0을 반환하는지 검증 */
    @Test
    fun accuracy_whenNoSolved_returnsZero() {
        val record = DailyRecord(date = "2026-04-22", solvedCount = 0, correctCount = 0, earnedXp = 0)
        assertEquals(0.0f, record.accuracy)
    }

    /** DailyRecord 생성 시 전달한 값이 그대로 저장되는지 검증 */
    @Test
    fun defaultValues_areCorrect() {
        val record = DailyRecord(date = "2026-04-22", solvedCount = 0, correctCount = 0, earnedXp = 0)
        assertEquals("2026-04-22", record.date)
        assertEquals(0, record.solvedCount)
        assertEquals(0, record.correctCount)
        assertEquals(0, record.earnedXp)
    }
}
