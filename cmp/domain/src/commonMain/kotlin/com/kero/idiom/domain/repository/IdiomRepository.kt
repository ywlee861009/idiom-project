package com.kero.idiom.domain.repository

import com.kero.idiom.domain.model.Idiom

interface IdiomRepository {
    suspend fun syncIfNeeded()
    suspend fun getRandomIdioms(limit: Int = 10): List<Idiom>
    suspend fun recordExposure(word: String)
    suspend fun recordCorrectAnswer(word: String)
    suspend fun getAllIdioms(): List<Idiom>
    suspend fun getAcquiredIdioms(): List<Idiom>
}
