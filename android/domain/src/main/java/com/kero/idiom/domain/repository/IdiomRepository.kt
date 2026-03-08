package com.kero.idiom.domain.repository

import com.kero.idiom.domain.model.Idiom

interface IdiomRepository {
    suspend fun getIdioms(): List<Idiom>
    suspend fun getRandomIdiom(): Idiom
}
