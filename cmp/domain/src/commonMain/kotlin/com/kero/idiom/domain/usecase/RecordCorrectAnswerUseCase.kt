package com.kero.idiom.domain.usecase

import com.kero.idiom.domain.repository.IdiomRepository

class RecordCorrectAnswerUseCase(
    private val repository: IdiomRepository
) {
    suspend operator fun invoke(word: String) {
        repository.recordCorrectAnswer(word)
    }
}
