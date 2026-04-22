package com.kero.idiom.domain.usecase

import com.kero.idiom.domain.repository.DailyRecordRepository
import com.kero.idiom.domain.repository.UserStatsRepository

class UpdateUserStatsUseCase(
    private val repository: UserStatsRepository,
    private val dailyRecordRepository: DailyRecordRepository
) {
    suspend operator fun invoke(correctCount: Int, solvedCount: Int, xpGained: Int, comboCount: Int) {
        repository.updateStats(correctCount, solvedCount, xpGained, comboCount)
        dailyRecordRepository.recordToday(
            solvedCount = solvedCount,
            correctCount = correctCount,
            earnedXp = xpGained
        )
    }
}
