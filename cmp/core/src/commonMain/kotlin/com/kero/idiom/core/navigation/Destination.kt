package com.kero.idiom.core.navigation

import kotlinx.serialization.Serializable

sealed interface Destination {
    @Serializable
    data object Quiz : Destination

    @Serializable
    data class Result(val score: Int) : Destination
}
