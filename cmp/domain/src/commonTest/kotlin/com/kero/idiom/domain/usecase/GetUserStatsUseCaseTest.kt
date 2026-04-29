package com.kero.idiom.domain.usecase

import com.kero.idiom.domain.model.UserStats
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

/** 사용자 통계 Flow 조회가 Repository의 실시간 데이터를 올바르게 방출하는지 검증 */
class GetUserStatsUseCaseTest {

    private val fakeRepo = FakeUserStatsRepository()
    private val useCase = GetUserStatsUseCase(fakeRepo)

    /** 초기 상태에서 기본 UserStats 값이 반환되는지 검증 */
    @Test
    fun invoke_returnsDefaultStats() = runTest {
        val stats = useCase().first()
        assertEquals(UserStats(), stats)
    }

    /** Repository 데이터 변경 후 업데이트된 통계가 Flow를 통해 반영되는지 검증 */
    @Test
    fun invoke_returnsUpdatedStats() = runTest {
        val expected = UserStats(
            totalSolvedCount = 10,
            totalCorrectCount = 8,
            level = 3,
            currentXp = 15,
            currentStreak = 2
        )
        fakeRepo.stats.value = expected

        val stats = useCase().first()
        assertEquals(expected, stats)
    }
}
