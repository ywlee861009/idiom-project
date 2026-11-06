package com.kero.idiom.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kero.idiom.BuildConfig
import com.kero.idiom.MainActivity
import com.kero.idiom.R
import com.kero.idiom.core.util.DateUtils
import com.kero.idiom.domain.repository.TodayLessonRepository
import com.kero.idiom.domain.repository.UserStatsRepository
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Calendar

class ReminderWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params), KoinComponent {
    private val userStatsRepository: UserStatsRepository by inject()
    private val todayLessonRepository: TodayLessonRepository by inject()

    override suspend fun doWork(): Result {
        if (!userStatsRepository.getUserStats().first().isNotificationEnabled ||
            !ReminderManager.canDeliver(applicationContext)
        ) return Result.success()

        // Migrate a previously queued reminder to the new daily job.
        if (!inputData.getBoolean(ReminderManager.PERIODIC_INPUT, false)) {
            ReminderManager.scheduleReminder(applicationContext)
            return Result.success()
        }

        val now = Calendar.getInstance()
        val day = DateUtils.getEpochDay(now.timeInMillis)
        if ((BuildConfig.DEBUG || now.get(Calendar.HOUR_OF_DAY) >= ReminderManager.REMINDER_HOUR) &&
            !todayLessonRepository.isCompleted(day)
        ) {
            currentCoroutineContext().ensureActive()
            showNotification(day)
            // A completion can race with notification delivery.
            if (todayLessonRepository.isCompleted(day)) {
                ReminderManager.clearNotification(applicationContext)
            }
        }

        currentCoroutineContext().ensureActive()
        if (userStatsRepository.getUserStats().first().isNotificationEnabled &&
            ReminderManager.canDeliver(applicationContext)
        ) {
            ReminderManager.scheduleNextMorning(applicationContext, id)
        }
        return Result.success()
    }

    @SuppressLint("MissingPermission")
    private fun showNotification(day: Long) {
        val context = applicationContext
        val manager = context.getSystemService(NotificationManager::class.java) ?: return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(
                NotificationChannel(ReminderManager.CHANNEL_ID, "학습 알림", NotificationManager.IMPORTANCE_DEFAULT)
            )
        }
        if (!ReminderManager.canDeliver(context) ||
            day != DateUtils.getEpochDay(DateUtils.getCurrentTimeMillis()) ||
            !ReminderManager.claimNotificationDay(context, day)
        ) return

        val intent = Intent(context, MainActivity::class.java).apply {
            action = ReminderManager.ACTION_OPEN_TODAY_LESSON
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notification = NotificationCompat.Builder(context, ReminderManager.CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setContentTitle("오늘의 서당")
            .setContentText("사자성어 3문제로 짧게 공부해 보세요.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        try {
            manager.notify(ReminderManager.NOTIFICATION_ID, notification)
        } catch (_: SecurityException) {
            // The system may revoke permission between the check and posting.
        }
    }
}
