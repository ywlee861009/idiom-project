package com.kero.idiom.domain.usecase

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

/** 퀴즈 완료 후 사용자 통계 업데이트 + 일일 기록 저장이 올바르게 동작하는지 검증 */
class UpdateUserStatsUseCaseTest {

    private val fakeUserStatsRepo = FakeUserStatsRepository()
    private val fakeDailyRecordRepo = FakeDailyRecordRepository()
    private val useCase = UpdateUserStatsUseCase(fakeUserStatsRepo, fakeDailyRecordRepo)

    /** 정답수, 풀이수, XP, 콤보가 UserStatsRepository에 정확히 전달되는지 검증 */
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

    /** 통계 업데이트와 동시에 DailyRecordRepository에도 기록이 저장되는지 검증 */
    @Test
    fun invoke_recordsDailyActivity() = runTest {
        useCase(correctCount = 3, solvedCount = 5, xpGained = 10, comboCount = 2)

        assertEquals(1, fakeDailyRecordRepo.recordTodayCalls.size)
        val (solved, correct, xp) = fakeDailyRecordRepo.recordTodayCalls[0]
        assertEquals(5, solved)
        assertEquals(3, correct)
        assertEquals(10, xp)
    }

    /** 여러 번 호출 시 각 호출이 독립적으로 누적 기록되는지 검증 */
    @Test
    fun invoke_multipleCallsAccumulate() = runTest {
        useCase(correctCount = 5, solvedCount = 5, xpGained = 15, comboCount = 5)
        useCase(correctCount = 3, solvedCount = 5, xpGained = 8, comboCount = 0)

        assertEquals(2, fakeUserStatsRepo.updateStatsCalls.size)
        assertEquals(2, fakeDailyRecordRepo.recordTodayCalls.size)
    }

    /** 모든 값이 0이어도 기록이 정상적으로 생성되는지 검증 (빈 세션 허용) */
    @Test
    fun invoke_withZeroValues_stillRecords() = runTest {
        useCase(correctCount = 0, solvedCount = 0, xpGained = 0, comboCount = 0)

        assertEquals(1, fakeUserStatsRepo.updateStatsCalls.size)
        assertEquals(1, fakeDailyRecordRepo.recordTodayCalls.size)
    }
}
