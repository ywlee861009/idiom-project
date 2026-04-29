package com.kero.idiom.domain.usecase

import com.kero.idiom.domain.model.DailyRecord
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** 주간 학습 통계 조회가 Repository 데이터를 올바르게 전달하는지 검증 */
class GetWeeklyStatsUseCaseTest {

    private val fakeRepo = FakeDailyRecordRepository()
    private val useCase = GetWeeklyStatsUseCase(fakeRepo)

    /** 7일치 기록이 있을 때 전체 데이터가 그대로 반환되는지 검증 */
    @Test
    fun invoke_returnsRecordsFromRepository() = runTest {
        val expected = listOf(
            DailyRecord("2026-04-16", 5, 4, 12),
            DailyRecord("2026-04-17", 5, 3, 8),
            DailyRecord("2026-04-18", 0, 0, 0),
            DailyRecord("2026-04-19", 10, 8, 25),
            DailyRecord("2026-04-20", 0, 0, 0),
            DailyRecord("2026-04-21", 5, 5, 15),
            DailyRecord("2026-04-22", 5, 2, 6),
        )
        fakeRepo.records.addAll(expected)

        val result = useCase()

        assertEquals(7, result.size)
        assertEquals(expected, result)
    }

    /** 기록이 없을 때 빈 리스트를 반환하는지 검증 (크래시 방지) */
    @Test
    fun invoke_whenEmpty_returnsEmptyList() = runTest {
        val result = useCase()

        assertTrue(result.isEmpty())
    }

    /** 7일 미만의 부분 데이터도 있는 그대로 반환되는지 검증 */
    @Test
    fun invoke_withPartialData_returnsAsIs() = runTest {
        val partial = listOf(
            DailyRecord("2026-04-20", 5, 3, 10),
            DailyRecord("2026-04-22", 5, 5, 15),
        )
        fakeRepo.records.addAll(partial)

        val result = useCase()

        assertEquals(2, result.size)
    }
}
