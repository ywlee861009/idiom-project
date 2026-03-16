package com.kero.idiom.core.util

import java.util.Calendar
import java.util.TimeZone

actual object DateUtils {
    actual fun getCurrentTimeMillis(): Long = System.currentTimeMillis()

    actual fun getEpochDay(millis: Long): Long {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        calendar.timeInMillis = millis
        val offset = calendar.timeZone.getOffset(millis)
        return (millis + offset) / (1000 * 60 * 60 * 24)
    }
}
