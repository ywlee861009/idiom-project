package com.kero.idiom.notification

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.kero.idiom.BuildConfig
import java.util.concurrent.TimeUnit

/**
 * 로컬 알림(리마인더) 스케줄을 관리하는 싱글톤 객체.
 */
object ReminderManager {
    private const val WORK_NAME = "idiom_reminder_work"

    /**
     * 알림을 예약합니다. 
     * 디버그 모드(Debug): 5분 뒤 알림 (테스트용)
     * 운영 모드(Release): 48시간 뒤 알림
     */
    fun scheduleReminder(context: Context) {
        // 빌드 타입에 따른 지연 시간 설정
        val (delay, unit) = if (BuildConfig.DEBUG) {
            5L to TimeUnit.MINUTES // 테스트를 위해 5분으로 설정
        } else {
            48L to TimeUnit.HOURS // 운영 시에는 48시간(2일)
        }

        val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delay, unit)
            .addTag(WORK_NAME)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            WORK_NAME,
            ExistingWorkPolicy.REPLACE, // 유저가 앱을 켰으므로 기존 작업 취소 후 새로 예약
            workRequest
        )
    }

    /**
     * 알림 예약을 취소합니다.
     */
    fun cancelReminder(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
    }
}
