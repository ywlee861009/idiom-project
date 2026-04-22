package com.kero.idiom.domain.usecase

import com.kero.idiom.domain.model.UserStats
import com.kero.idiom.domain.repository.UserStatsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeUserStatsRepository : UserStatsRepository {
    val stats = MutableStateFlow(UserStats())
    var updateStatsCalls = mutableListOf<UpdateStatsCall>()

    data class UpdateStatsCall(
        val correctCount: Int,
        val solvedCount: Int,
        val xpGained: Int,
        val comboCount: Int
    )

    override fun getUserStats(): Flow<UserStats> = stats

    override suspend fun updateStats(correctCount: Int, solvedCount: Int, xpGained: Int, comboCount: Int) {
        updateStatsCalls.add(UpdateStatsCall(correctCount, solvedCount, xpGained, comboCount))
    }

    override suspend fun updateNotificationEnabled(enabled: Boolean) {}
}
