package com.kero.idiom.ui.profile.contract

import com.kero.idiom.domain.model.UserStats

data class ProfileState(
    val userStats: UserStats = UserStats(),
    val isLoading: Boolean = false
)

sealed interface ProfileIntent {
    data object Refresh : ProfileIntent
}
