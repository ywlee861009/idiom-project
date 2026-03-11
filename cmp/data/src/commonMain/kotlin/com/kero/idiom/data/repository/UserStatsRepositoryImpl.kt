package com.kero.idiom.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import com.kero.idiom.data.local.dao.UserStatsDao
import com.kero.idiom.data.local.model.UserStatsEntity
import com.kero.idiom.domain.model.UserStats
import com.kero.idiom.domain.repository.UserStatsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class UserStatsRepositoryImpl(
    private val userStatsDao: UserStatsDao,
    private val dataStore: DataStore<Preferences> // 📜 서책 버전 확인용
) : UserStatsRepository {

    private val IDIOM_VERSION = intPreferencesKey("idiom_version")

    override fun getUserStats(): Flow<UserStats> {
        val statsFlow = userStatsDao.getUserStatsFlow().map { it ?: UserStatsEntity() }
        val versionFlow = dataStore.data.map { it[IDIOM_VERSION] ?: 0 }

        // DB 통계와 DataStore 버전을 결합!
        return statsFlow.combine(versionFlow) { entity, version ->
            entity.toDomain(version)
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

    private fun UserStatsEntity.toDomain(version: Int) = UserStats(
        totalSolvedCount = totalSolvedCount,
        currentStreak = currentStreak,
        maxStreak = maxStreak,
        lastSolvedDateMillis = lastSolvedDateMillis,
        totalCorrectCount = totalCorrectCount,
        isNotificationEnabled = isNotificationEnabled,
        dataVersion = version
    )
}
