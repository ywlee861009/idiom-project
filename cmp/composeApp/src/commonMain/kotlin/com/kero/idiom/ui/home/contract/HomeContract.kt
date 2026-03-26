package com.kero.idiom.ui.home.contract

import com.kero.idiom.domain.model.Idiom
import com.kero.idiom.domain.model.UserStats

data class HomeState(
    val userStats: UserStats = UserStats(),
    val todayIdiom: Idiom? = null,
    val isLoading: Boolean = true
)

sealed interface HomeIntent {
    data object LoadTodayIdiom : HomeIntent
    data object Refresh : HomeIntent
}
