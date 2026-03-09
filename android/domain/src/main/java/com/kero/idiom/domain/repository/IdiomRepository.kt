package com.kero.idiom.domain.repository

import com.kero.idiom.domain.model.Idiom

interface IdiomRepository {
    suspend fun syncIfNeeded()
    suspend fun getRandomIdioms(limit: Int = 10): List<Idiom>
    suspend fun recordExposure(word: String)
}
