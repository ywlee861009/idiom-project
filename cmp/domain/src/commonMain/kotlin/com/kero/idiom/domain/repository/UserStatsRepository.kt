package com.kero.idiom.domain.repository

import com.kero.idiom.domain.model.UserStats
import kotlinx.coroutines.flow.Flow

interface UserStatsRepository {
    fun getUserStats(): Flow<UserStats>
    suspend fun updateStats(correctCount: Int, solvedCount: Int)
    suspend fun updateNotificationEnabled(enabled: Boolean)
}
