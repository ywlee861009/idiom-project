package com.kero.idiom.domain.repository

import com.kero.idiom.domain.model.Idiom

interface IdiomRepository {
    /**
     * @param onStatusChanged 동기화 진행 상태를 문자열 메시지로 전달받는 콜백
     */
    suspend fun syncIfNeeded(onStatusChanged: ((String) -> Unit)? = null)
    
    suspend fun getRandomIdioms(limit: Int = 10): List<Idiom>
    suspend fun recordExposure(word: String)
    suspend fun recordCorrectAnswer(word: String)
    suspend fun getAllIdioms(): List<Idiom>
    suspend fun getAcquiredIdioms(): List<Idiom>
}
