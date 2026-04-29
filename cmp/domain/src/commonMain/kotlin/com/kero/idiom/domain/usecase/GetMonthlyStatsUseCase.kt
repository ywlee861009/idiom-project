package com.kero.idiom.domain.usecase

import com.kero.idiom.domain.model.DailyRecord
import com.kero.idiom.domain.repository.DailyRecordRepository

class GetMonthlyStatsUseCase(
    private val repository: DailyRecordRepository
) {
    suspend operator fun invoke(yearMonth: String): List<DailyRecord> {
        return repository.getMonthlyRecords(yearMonth)
    }
}
