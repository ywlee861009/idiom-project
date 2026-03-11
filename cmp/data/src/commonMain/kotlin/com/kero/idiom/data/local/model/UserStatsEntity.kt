package com.kero.idiom.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_stats")
data class UserStatsEntity(
    @PrimaryKey val id: Int = 0, // 싱글톤으로 관리하기 위해 0번 ID만 사용
    val totalSolvedCount: Int = 0,
    val currentStreak: Int = 0,
    val maxStreak: Int = 0,
    val lastSolvedDateMillis: Long = 0,
    val totalCorrectCount: Int = 0,
    val isNotificationEnabled: Boolean = true
)
