package com.kero.idiom.domain.repository

import com.kero.idiom.domain.model.DailyRecord

interface DailyRecordRepository {
    suspend fun recordToday(solvedCount: Int, correctCount: Int, earnedXp: Int)
    suspend fun getWeeklyRecords(): List<DailyRecord>
    suspend fun getMonthlyRecords(yearMonth: String): List<DailyRecord>
}
