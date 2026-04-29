package com.kero.idiom.core.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

actual object DateUtils {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    actual fun getCurrentTimeMillis(): Long = System.currentTimeMillis()

    actual fun getEpochDay(millis: Long): Long {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        calendar.timeInMillis = millis
        val offset = calendar.timeZone.getOffset(millis)
        return (millis + offset) / (1000 * 60 * 60 * 24)
    }

    actual fun getTodayDateString(): String {
        return dateFormat.format(Calendar.getInstance().time)
    }

    actual fun getDateStringDaysAgo(daysAgo: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)
        return dateFormat.format(calendar.time)
    }

    actual fun getDayOfWeekLabel(dateString: String): String {
        val date = dateFormat.parse(dateString) ?: return ""
        val calendar = Calendar.getInstance()
        calendar.time = date
        return when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> "월"
            Calendar.TUESDAY -> "화"
            Calendar.WEDNESDAY -> "수"
            Calendar.THURSDAY -> "목"
            Calendar.FRIDAY -> "금"
            Calendar.SATURDAY -> "토"
            Calendar.SUNDAY -> "일"
            else -> ""
        }
    }

    private val yearMonthFormat = SimpleDateFormat("yyyy-MM", Locale.getDefault())

    actual fun getYearMonthString(): String {
        return yearMonthFormat.format(Calendar.getInstance().time)
    }

    actual fun getFirstDayOfWeekInMonth(yearMonth: String): Int {
        val parts = yearMonth.split("-")
        val calendar = Calendar.getInstance()
        calendar.clear()
        calendar.set(Calendar.YEAR, parts[0].toInt())
        calendar.set(Calendar.MONTH, parts[1].toInt() - 1)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        // Calendar.SUNDAY=1 -> 0, MONDAY=2 -> 1, ... SATURDAY=7 -> 6
        return calendar.get(Calendar.DAY_OF_WEEK) - 1 // 일=0, 월=1, ..., 토=6
    }

    actual fun getDaysInMonth(yearMonth: String): Int {
        val parts = yearMonth.split("-")
        val calendar = Calendar.getInstance()
        calendar.clear()
        calendar.set(Calendar.YEAR, parts[0].toInt())
        calendar.set(Calendar.MONTH, parts[1].toInt() - 1)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    actual fun getYearMonthOffset(yearMonth: String, offset: Int): String {
        val parts = yearMonth.split("-")
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, parts[0].toInt())
        calendar.set(Calendar.MONTH, parts[1].toInt() - 1)
        calendar.add(Calendar.MONTH, offset)
        return yearMonthFormat.format(calendar.time)
    }

    actual fun getDayOfMonth(dateString: String): Int {
        val date = dateFormat.parse(dateString) ?: return 0
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.get(Calendar.DAY_OF_MONTH)
    }
}
