package com.kero.idiom.domain.usecase

import com.kero.idiom.domain.model.UserStats
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetUserStatsUseCaseTest {

    private val fakeRepo = FakeUserStatsRepository()
    private val useCase = GetUserStatsUseCase(fakeRepo)

    @Test
    fun invoke_returnsDefaultStats() = runTest {
        val stats = useCase().first()
        assertEquals(UserStats(), stats)
    }

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
