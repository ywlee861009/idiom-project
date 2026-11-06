package com.kero.idiom.ui.home.contract

import com.kero.idiom.domain.model.UserStats

data class HomeState(
    val userStats: UserStats = UserStats(),
    val dateLabel: String? = null,
    val lessonTheme: String? = null,
    val started: Boolean = false,
    val answeredCount: Int = 0,
    val completed: Boolean = false,
    val completedDays: Int? = null
)
