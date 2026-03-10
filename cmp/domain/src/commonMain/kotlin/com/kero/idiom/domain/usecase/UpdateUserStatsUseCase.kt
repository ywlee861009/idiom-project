package com.kero.idiom.domain.usecase

import com.kero.idiom.domain.repository.UserStatsRepository

class UpdateUserStatsUseCase(
    private val repository: UserStatsRepository
) {
    suspend operator fun invoke(correctCount: Int, solvedCount: Int) {
        repository.updateStats(correctCount, solvedCount)
    }
}
