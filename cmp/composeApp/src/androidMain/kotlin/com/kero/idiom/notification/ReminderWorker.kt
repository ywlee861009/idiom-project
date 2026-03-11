package com.kero.idiom.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kero.idiom.MainActivity
import com.kero.idiom.R
import com.kero.idiom.domain.repository.UserStatsRepository
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * 48시간 동안 앱을 열지 않았을 때 로컬 알림을 생성하는 워커.
 */
class ReminderWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params), KoinComponent {

    private val userStatsRepository: UserStatsRepository by inject()

    override suspend fun doWork(): Result {
        // [방어 로직] 실행 시점에 DB 설정이 꺼져 있으면 즉시 중단 (이중 잠금)
        val stats = userStatsRepository.getUserStats().first()
        if (!stats.isNotificationEnabled) {
            return Result.success()
        }

        showNotification()

        // [추가] 알림을 보낸 후, 사용자가 앱을 열지 않더라도 2일 뒤에 다시 알림이 울리도록 재예약
        // 사용자가 앱을 열면 MainActivity에서 이 예약을 REPLACE하여 다시 2일 뒤로 밀게 됩니다.
        ReminderManager.scheduleReminder(applicationContext)

        return Result.success()
    }

    private fun showNotification() {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "idiom_reminder_channel"

        // 안드로이드 8.0 이상은 채널 필수
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "학습 리마인더",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "사자성어 학습을 잊지 않도록 알림을 보냅니다."
            }
            notificationManager.createNotificationChannel(channel)
        }

        // 알림 클릭 시 앱 실행
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // 무작위 메시지 선택
        val message = ReminderMessage.getRandom()

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.mipmap.ic_launcher_foreground) // [수정] 배경이 투명한 실루엣 아이콘 사용
            .setColor(applicationContext.getColor(android.R.color.black)) // [추가] 아이콘에 입힐 강조색 (먹색 권장)
            .setContentTitle(message.title)
            .setContentText(message.content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1001, notification)
    }
}
