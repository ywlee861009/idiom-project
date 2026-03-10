package com.kero.idiom.data.local.dao

import androidx.room.*
import com.kero.idiom.data.local.model.UserStatsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserStatsDao {
    @Query("SELECT * FROM user_stats WHERE id = 0")
    fun getUserStatsFlow(): Flow<UserStatsEntity?>

    @Query("SELECT * FROM user_stats WHERE id = 0")
    suspend fun getUserStats(): UserStatsEntity?

    @Upsert
    suspend fun upsertUserStats(userStats: UserStatsEntity)
}
