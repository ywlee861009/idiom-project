package com.kero.idiom.data.repository

import com.kero.idiom.data.local.dao.UserStatsDao
import com.kero.idiom.data.local.model.UserStatsEntity
import com.kero.idiom.domain.model.UserStats
import com.kero.idiom.domain.repository.UserStatsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserStatsRepositoryImpl(
    private val userStatsDao: UserStatsDao
) : UserStatsRepository {

    override fun getUserStats(): Flow<UserStats> {
        return userStatsDao.getUserStatsFlow().map { entity ->
            entity?.toDomain() ?: UserStats()
        }
    }

    override suspend fun updateStats(correctCount: Int, solvedCount: Int) {
        val currentEntity = userStatsDao.getUserStats() ?: UserStatsEntity()
        val now = System.currentTimeMillis()
        
        val lastSolved = currentEntity.lastSolvedDateMillis
        val diff = now - lastSolved
        val oneDayMillis = 24 * 60 * 60 * 1000L
        
        val newStreak = when {
            lastSolved == 0L -> 1
            diff < oneDayMillis -> currentEntity.currentStreak
            diff < 2 * oneDayMillis -> currentEntity.currentStreak + 1
            else -> 1
        }

        val updatedEntity = currentEntity.copy(
            totalSolvedCount = currentEntity.totalSolvedCount + solvedCount,
            totalCorrectCount = currentEntity.totalCorrectCount + correctCount,
            currentStreak = newStreak,
            maxStreak = maxOf(currentEntity.maxStreak, newStreak),
            lastSolvedDateMillis = now
        )
        
        userStatsDao.upsertUserStats(updatedEntity)
    }

    override suspend fun updateNotificationEnabled(enabled: Boolean) {
        val currentEntity = userStatsDao.getUserStats() ?: UserStatsEntity()
        userStatsDao.upsertUserStats(currentEntity.copy(isNotificationEnabled = enabled))
    }

    private fun UserStatsEntity.toDomain() = UserStats(
        totalSolvedCount = totalSolvedCount,
        currentStreak = currentStreak,
        maxStreak = maxStreak,
        lastSolvedDateMillis = lastSolvedDateMillis,
        totalCorrectCount = totalCorrectCount,
        isNotificationEnabled = isNotificationEnabled
    )
}
