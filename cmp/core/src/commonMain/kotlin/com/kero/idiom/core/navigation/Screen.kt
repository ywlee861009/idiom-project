package com.kero.idiom.core.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {
    @Serializable
    data object Home : Screen

    @Serializable
    data object Collection : Screen

    @Serializable
    data object Profile : Screen

    @Serializable
    data object Quiz : Screen

    @Serializable
    data class Reward(val score: Int, val total: Int) : Screen
}
