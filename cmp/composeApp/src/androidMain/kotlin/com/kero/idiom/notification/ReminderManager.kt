package com.kero.idiom.notification

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.kero.idiom.BuildConfig
import java.util.Calendar
import java.util.concurrent.TimeUnit

/**
 * 로컬 알림(리마인더) 스케줄을 관리하는 싱글톤 객체.
 */
object ReminderManager {
    private const val WORK_NAME = "idiom_reminder_work"

    /**
     * 알림을 예약합니다. 
     * 디버그 모드(Debug): 5분 뒤 알림 (테스트용)
     * 운영 모드(Release): 2일 뒤 오전 8시 알림
     */
    fun scheduleReminder(context: Context) {
        val (delay, unit) = if (BuildConfig.DEBUG) {
            5L to TimeUnit.MINUTES // 테스트를 위해 5분으로 설정
        } else {
            calculateDelayUntilEightAm() to TimeUnit.MILLISECONDS
        }

        val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delay, unit)
            .addTag(WORK_NAME)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }

    /**
     * 현재 시간으로부터 '2일 뒤 오전 8시'까지의 밀리초를 계산합니다.
     */
    private fun calculateDelayUntilEightAm(): Long {
        val now = Calendar.getInstance()
        
        // 목표 시간 설정 (2일 뒤 오전 8시)
        val target = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, 2)
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // 혹시라도 계산상 현재보다 과거가 되면 (그럴 리 없지만 방어코드) 1일 더함
        if (target.before(now)) {
            target.add(Calendar.DAY_OF_YEAR, 1)
        }

        return target.timeInMillis - now.timeInMillis
    }

    /**
     * 알림 예약을 취소합니다.
     */
    fun cancelReminder(context: Context) {
        val workManager = WorkManager.getInstance(context)
        // [강력 취소] 유니크 워크와 태그 기반 작업을 모두 제거
        workManager.cancelUniqueWork(WORK_NAME)
        workManager.cancelAllWorkByTag(WORK_NAME)
    }
}
