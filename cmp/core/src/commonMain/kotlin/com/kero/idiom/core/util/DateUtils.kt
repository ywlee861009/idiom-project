package com.kero.idiom.core.util

expect object DateUtils {
    fun getCurrentTimeMillis(): Long
    fun getEpochDay(millis: Long): Long
    fun getTodayDateString(): String
    fun getDateStringDaysAgo(daysAgo: Int): String
    fun getDayOfWeekLabel(dateString: String): String
    fun getYearMonthString(): String
    fun getFirstDayOfWeekInMonth(yearMonth: String): Int
    fun getDaysInMonth(yearMonth: String): Int
    fun getYearMonthOffset(yearMonth: String, offset: Int): String
    fun getDayOfMonth(dateString: String): Int
}
