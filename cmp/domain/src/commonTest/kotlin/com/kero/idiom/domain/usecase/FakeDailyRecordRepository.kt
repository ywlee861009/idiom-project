package com.kero.idiom.domain.usecase

import com.kero.idiom.domain.model.DailyRecord
import com.kero.idiom.domain.repository.DailyRecordRepository

class FakeDailyRecordRepository : DailyRecordRepository {
    val records = mutableListOf<DailyRecord>()
    val monthlyRecords = mutableMapOf<String, List<DailyRecord>>()
    var recordTodayCalls = mutableListOf<Triple<Int, Int, Int>>()

    override suspend fun recordToday(solvedCount: Int, correctCount: Int, earnedXp: Int) {
        recordTodayCalls.add(Triple(solvedCount, correctCount, earnedXp))
    }

    override suspend fun getWeeklyRecords(): List<DailyRecord> = records

    override suspend fun getMonthlyRecords(yearMonth: String): List<DailyRecord> {
        return monthlyRecords[yearMonth] ?: emptyList()
    }
}
