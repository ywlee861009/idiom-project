package com.kero.idiom.core.util

expect object DateUtils {
    fun getCurrentTimeMillis(): Long
    fun getEpochDay(millis: Long): Long
}
