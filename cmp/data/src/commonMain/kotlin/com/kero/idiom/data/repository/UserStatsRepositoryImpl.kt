package com.kero.idiom.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.kero.idiom.domain.model.UserStats
import com.kero.idiom.domain.repository.UserStatsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * DataStore를 사용하여 사용자의 핵심 학습 통계를 관리하는 저장소 구현체.
 * Key-Value 방식을 사용하여 DB 마이그레이션 예외로부터 데이터를 안전하게 보호합니다.
 */
class UserStatsRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : UserStatsRepository {

    private object Keys {
        val TOTAL_SOLVED = intPreferencesKey("total_solved_count")
        val TOTAL_CORRECT = intPreferencesKey("total_correct_count")
        val CURRENT_STREAK = intPreferencesKey("current_streak")
        val MAX_STREAK = intPreferencesKey("max_streak")
        val LAST_SOLVED_DATE = longPreferencesKey("last_solved_date_millis")
        val NOTIFICATION_ENABLED = booleanPreferencesKey("is_notification_enabled")
        val DATA_VERSION = intPreferencesKey("user_data_version")
        val LEVEL = intPreferencesKey("current_level")
        val CURRENT_XP = intPreferencesKey("current_xp")
    }

    override fun getUserStats(): Flow<UserStats> {
        return dataStore.data.map { prefs ->
            UserStats(
                totalSolvedCount = prefs[Keys.TOTAL_SOLVED] ?: 0,
                totalCorrectCount = prefs[Keys.TOTAL_CORRECT] ?: 0,
                currentStreak = prefs[Keys.CURRENT_STREAK] ?: 0,
                maxStreak = prefs[Keys.MAX_STREAK] ?: 0,
                lastSolvedDateMillis = prefs[Keys.LAST_SOLVED_DATE] ?: 0L,
                isNotificationEnabled = prefs[Keys.NOTIFICATION_ENABLED] ?: true,
                dataVersion = prefs[Keys.DATA_VERSION] ?: 0,
                level = prefs[Keys.LEVEL] ?: 1,
                currentXp = prefs[Keys.CURRENT_XP] ?: 0
            )
        }
    }

    override suspend fun updateStats(correctCount: Int, solvedCount: Int, xpGained: Int) {
        dataStore.edit { prefs ->
            val currentSolved = prefs[Keys.TOTAL_SOLVED] ?: 0
            val currentCorrect = prefs[Keys.TOTAL_CORRECT] ?: 0
            var currentLevel = prefs[Keys.LEVEL] ?: 1
            var currentXp = prefs[Keys.CURRENT_XP] ?: 0
            
            prefs[Keys.TOTAL_SOLVED] = currentSolved + solvedCount
            prefs[Keys.TOTAL_CORRECT] = currentCorrect + correctCount
            
            // 🌟 레벨 및 경험치 개편 로직
            var newXp = currentXp + xpGained
            // 공식: 다음 레벨에 필요한 XP = 현재 레벨 * 20
            while (newXp >= currentLevel * 20) {
                newXp -= currentLevel * 20
                currentLevel++
            }
            
            prefs[Keys.LEVEL] = currentLevel
            prefs[Keys.CURRENT_XP] = newXp
            
            // 💡 여기서 스트릭(Streak) 계산 로직 등을 추가할 수 있습니다.
        }
    }

    override suspend fun updateNotificationEnabled(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[Keys.NOTIFICATION_ENABLED] = enabled
        }
    }
}
