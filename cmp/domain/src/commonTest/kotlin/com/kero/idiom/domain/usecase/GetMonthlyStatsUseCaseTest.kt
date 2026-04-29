package com.kero.idiom.domain.usecase

import com.kero.idiom.domain.model.DailyRecord
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetMonthlyStatsUseCaseTest {

    private val fakeRepo = FakeDailyRecordRepository()
    private val useCase = GetMonthlyStatsUseCase(fakeRepo)

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

    @Test
    fun invoke_noData_returnsEmpty() = runTest {
        val result = useCase("2026-03")
        assertTrue(result.isEmpty())
    }

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
