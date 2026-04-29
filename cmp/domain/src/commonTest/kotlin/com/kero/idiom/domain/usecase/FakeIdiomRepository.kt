package com.kero.idiom.domain.usecase

import com.kero.idiom.domain.model.Idiom
import com.kero.idiom.domain.repository.IdiomRepository

class FakeIdiomRepository : IdiomRepository {
    val idioms = mutableListOf<Idiom>()
    val exposedWords = mutableListOf<String>()
    val correctAnswerWords = mutableListOf<String>()

    override suspend fun syncIfNeeded(onStatusChanged: ((String) -> Unit)?) {}

    override suspend fun getRandomIdioms(limit: Int): List<Idiom> {
        return idioms.shuffled().take(limit)
    }

    override suspend fun recordExposure(word: String) {
        exposedWords.add(word)
    }

    override suspend fun recordCorrectAnswer(word: String) {
        correctAnswerWords.add(word)
    }

    override suspend fun getAllIdioms(): List<Idiom> = idioms

    override suspend fun getAcquiredIdioms(): List<Idiom> = idioms.filter { it.word in correctAnswerWords }
}
