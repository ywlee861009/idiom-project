package com.kero.idiom.data.repository

import com.kero.idiom.core.util.DateUtils
import com.kero.idiom.data.local.model.DailyRecordEntity
import com.kero.idiom.domain.model.DailyRecord
import com.kero.idiom.domain.repository.DailyRecordRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query

class DailyRecordRepositoryImpl(
    private val realm: Realm
) : DailyRecordRepository {

    override suspend fun recordToday(solvedCount: Int, correctCount: Int, earnedXp: Int) {
        val today = DateUtils.getTodayDateString()
        realm.write {
            val existing = query<DailyRecordEntity>("date == $0", today).first().find()
            if (existing != null) {
                existing.solvedCount += solvedCount
                existing.correctCount += correctCount
                existing.earnedXp += earnedXp
            } else {
                copyToRealm(DailyRecordEntity().apply {
                    this.date = today
                    this.solvedCount = solvedCount
                    this.correctCount = correctCount
                    this.earnedXp = earnedXp
                })
            }
        }
    }

    override suspend fun getMonthlyRecords(yearMonth: String): List<DailyRecord> {
        val startDate = "$yearMonth-01"
        val daysInMonth = DateUtils.getDaysInMonth(yearMonth)
        val endDate = "$yearMonth-${daysInMonth.toString().padStart(2, '0')}"

        return realm.query<DailyRecordEntity>(
            "date >= $0 AND date <= $1", startDate, endDate
        ).find().map { entity ->
            DailyRecord(
                date = entity.date,
                solvedCount = entity.solvedCount,
                correctCount = entity.correctCount,
                earnedXp = entity.earnedXp
            )
        }
    }

    override suspend fun getWeeklyRecords(): List<DailyRecord> {
        val today = DateUtils.getTodayDateString()
        val weekAgo = DateUtils.getDateStringDaysAgo(6)

        val records = realm.query<DailyRecordEntity>(
            "date >= $0 AND date <= $1", weekAgo, today
        ).find().map { entity ->
            DailyRecord(
                date = entity.date,
                solvedCount = entity.solvedCount,
                correctCount = entity.correctCount,
                earnedXp = entity.earnedXp
            )
        }

        // 7일치 빈 날짜 포함하여 반환
        val recordMap = records.associateBy { it.date }
        return (6 downTo 0).map { daysAgo ->
            val date = DateUtils.getDateStringDaysAgo(daysAgo)
            recordMap[date] ?: DailyRecord(date = date, solvedCount = 0, correctCount = 0, earnedXp = 0)
        }
    }
}
