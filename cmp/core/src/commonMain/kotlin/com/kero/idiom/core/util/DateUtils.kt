package com.kero.idiom.core.util

expect object DateUtils {
    fun getCurrentTimeMillis(): Long
    fun getEpochDay(millis: Long): Long
    fun getTodayDateString(): String
    fun getDateStringDaysAgo(daysAgo: Int): String
    fun getDayOfWeekLabel(dateString: String): String
}
