package com.kero.idiom.domain.usecase

import com.kero.idiom.domain.model.DailyRecord
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** 월간 학습 통계 조회�� yearMonth 키로 올바른 데이터를 반환하는지 검증 */
class GetMonthlyStatsUseCaseTest {

    private val fakeRepo = FakeDailyRecordRepository()
    private val useCase = GetMonthlyStatsUseCase(fakeRepo)

    /** 해당 월의 기록이 있을 때 전체 데이터가 반환되는지 검증 */
    @Test
    fun invoke_returnsRecordsForMonth() = runTest {
        val aprilRecords = listOf(
            DailyRecord("2026-04-01", 5, 4, 12),
            DailyRecord("2026-04-15", 10, 8, 25),
            DailyRecord("2026-04-30", 5, 5, 15),
        )
        fakeRepo.monthlyRecords["2026-04"] = aprilRecords

        val result = useCase("2026-04")

        assertEquals(3, result.size)
        assertEquals(aprilRecords, result)
    }

    /** 기록이 없는 월을 조회할 때 빈 리스트를 반환하는지 검증 (크래시 방지) */
    @Test
    fun invoke_noData_returnsEmpty() = runTest {
        val result = useCase("2026-03")
        assertTrue(result.isEmpty())
    }

    /** 서로 다른 월의 데이터가 혼동 없이 각각 올바르게 반환되는지 검증 */
    @Test
    fun invoke_differentMonths_returnCorrectData() = runTest {
        fakeRepo.monthlyRecords["2026-04"] = listOf(
            DailyRecord("2026-04-01", 5, 4, 12)
        )
        fakeRepo.monthlyRecords["2026-05"] = listOf(
            DailyRecord("2026-05-01", 10, 9, 30)
        )

        val april = useCase("2026-04")
        val may = useCase("2026-05")

        assertEquals(1, april.size)
        assertEquals(5, april[0].solvedCount)
        assertEquals(1, may.size)
        assertEquals(10, may[0].solvedCount)
    }
}
