package com.kero.idiom.ui.profile.contract

import com.kero.idiom.domain.model.DailyRecord
import com.kero.idiom.domain.model.UserStats

data class ProfileState(
    val userStats: UserStats = UserStats(),
    val weeklyRecords: List<DailyRecord> = emptyList(),
    val monthlyRecords: List<DailyRecord> = emptyList(),
    val selectedYearMonth: String = "",
    val isLoading: Boolean = false
)

sealed interface ProfileIntent {
    data object Refresh : ProfileIntent
    data class ToggleNotification(val enabled: Boolean) : ProfileIntent
    data object PreviousMonth : ProfileIntent
    data object NextMonth : ProfileIntent
}
