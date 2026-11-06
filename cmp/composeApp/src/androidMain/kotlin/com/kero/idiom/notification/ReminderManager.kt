package com.kero.idiom.notification

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.kero.idiom.BuildConfig
import java.util.Calendar
import java.util.UUID
import java.util.concurrent.TimeUnit

object ReminderManager {
    const val ACTION_OPEN_TODAY_LESSON = "com.kero.idiom.action.OPEN_TODAY_LESSON"
    internal const val CHANNEL_ID = "idiom_reminder_channel"
    internal const val NOTIFICATION_ID = 1001
    internal const val PERIODIC_INPUT = "today_lesson_periodic"
    internal const val REMINDER_HOUR = 8
    private const val WORK_NAME = "today_lesson_reminder"
    private const val LEGACY_WORK_NAME = "idiom_reminder_work"

    fun scheduleReminder(context: Context) {
        val workManager = WorkManager.getInstance(context)
        workManager.cancelUniqueWork(LEGACY_WORK_NAME)
        val delay = if (BuildConfig.DEBUG) TimeUnit.MINUTES.toMillis(1)
        else nextMorningMillis() - System.currentTimeMillis()
        workManager.enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            reminderRequest().setInitialDelay(delay.coerceAtLeast(0), TimeUnit.MILLISECONDS).build()
        )
    }

    internal fun scheduleNextMorning(context: Context, workId: UUID) {
        if (BuildConfig.DEBUG) return
        WorkManager.getInstance(context).updateWork(
            reminderRequest()
                .setId(workId)
                .setNextScheduleTimeOverride(nextMorningMillis())
                .build()
        )
    }

    fun cancelReminder(context: Context) {
        val workManager = WorkManager.getInstance(context)
        workManager.cancelUniqueWork(WORK_NAME)
        workManager.cancelUniqueWork(LEGACY_WORK_NAME)
        clearNotification(context)
    }

    fun clearNotification(context: Context) {
        NotificationManagerCompat.from(context).cancel(NOTIFICATION_ID)
    }

    fun canDeliver(context: Context): Boolean {
        if (!NotificationManagerCompat.from(context).areNotificationsEnabled()) return false
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return true
        val manager = context.getSystemService(NotificationManager::class.java) ?: return false
        return manager.getNotificationChannel(CHANNEL_ID)?.importance != NotificationManager.IMPORTANCE_NONE
    }

    @Synchronized
    internal fun claimNotificationDay(context: Context, day: Long): Boolean {
        val preferences = context.getSharedPreferences(WORK_NAME, Context.MODE_PRIVATE)
        val dateKey = day.toString()
        if (preferences.getBoolean(dateKey, false)) return false
        return preferences.edit().putBoolean(dateKey, true).commit()
    }

    private fun reminderRequest() = PeriodicWorkRequestBuilder<ReminderWorker>(
        if (BuildConfig.DEBUG) 15 else TimeUnit.DAYS.toMinutes(1),
        TimeUnit.MINUTES
    ).setInputData(workDataOf(PERIODIC_INPUT to true))

    private fun nextMorningMillis(): Long {
        val now = Calendar.getInstance()
        return (now.clone() as Calendar).apply {
            set(Calendar.HOUR_OF_DAY, REMINDER_HOUR)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (!after(now)) add(Calendar.DAY_OF_YEAR, 1)
        }.timeInMillis
    }
}
