package com.kero.idiom.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Idiom(
    val word: String,
    val hanja: String,
    val meaning: String,
    val level: Int
)
