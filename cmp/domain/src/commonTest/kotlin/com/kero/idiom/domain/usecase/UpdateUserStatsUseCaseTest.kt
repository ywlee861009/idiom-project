package com.kero.idiom.domain.usecase

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class UpdateUserStatsUseCaseTest {

    private val fakeUserStatsRepo = FakeUserStatsRepository()
    private val fakeDailyRecordRepo = FakeDailyRecordRepository()
    private val useCase = UpdateUserStatsUseCase(fakeUserStatsRepo, fakeDailyRecordRepo)

    @Test
    fun invoke_updatesUserStats() = runTest {
        useCase(correctCount = 4, solvedCount = 5, xpGained = 12, comboCount = 3)

        assertEquals(1, fakeUserStatsRepo.updateStatsCalls.size)
        val call = fakeUserStatsRepo.updateStatsCalls[0]
        assertEquals(4, call.correctCount)
        assertEquals(5, call.solvedCount)
        assertEquals(12, call.xpGained)
        assertEquals(3, call.comboCount)
    }

    @Test
    fun invoke_recordsDailyActivity() = runTest {
        useCase(correctCount = 3, solvedCount = 5, xpGained = 10, comboCount = 2)

        assertEquals(1, fakeDailyRecordRepo.recordTodayCalls.size)
        val (solved, correct, xp) = fakeDailyRecordRepo.recordTodayCalls[0]
        assertEquals(5, solved)
        assertEquals(3, correct)
        assertEquals(10, xp)
    }

    @Test
    fun invoke_multipleCallsAccumulate() = runTest {
        useCase(correctCount = 5, solvedCount = 5, xpGained = 15, comboCount = 5)
        useCase(correctCount = 3, solvedCount = 5, xpGained = 8, comboCount = 0)

        assertEquals(2, fakeUserStatsRepo.updateStatsCalls.size)
        assertEquals(2, fakeDailyRecordRepo.recordTodayCalls.size)
    }

    @Test
    fun invoke_withZeroValues_stillRecords() = runTest {
        useCase(correctCount = 0, solvedCount = 0, xpGained = 0, comboCount = 0)

        assertEquals(1, fakeUserStatsRepo.updateStatsCalls.size)
        assertEquals(1, fakeDailyRecordRepo.recordTodayCalls.size)
    }
}
