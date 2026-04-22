package com.kero.idiom.domain.usecase

import com.kero.idiom.domain.model.DailyRecord
import com.kero.idiom.domain.repository.DailyRecordRepository

class GetWeeklyStatsUseCase(
    private val repository: DailyRecordRepository
) {
    suspend operator fun invoke(): List<DailyRecord> {
        return repository.getWeeklyRecords()
    }
}
