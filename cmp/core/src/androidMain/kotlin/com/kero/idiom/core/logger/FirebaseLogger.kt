package com.kero.idiom.core.logger

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics

/**
 * Android 전용 Firebase 로거 구현체.
 */
class FirebaseLogger(context: Context) : IdiomLogger {
    private val analytics = FirebaseAnalytics.getInstance(context)
    private val crashlytics = FirebaseCrashlytics.getInstance()

    override fun log(message: String) {
        // Crashlytics 커스텀 로그 기록 (비정상 종료 시 함께 전송됨)
        crashlytics.log(message)
    }

    override fun recordException(throwable: Throwable) {
        // 비정상 종료는 아니지만 예외 발생 시 기록
        crashlytics.recordException(throwable)
    }

    override fun logEvent(name: String, params: Map<String, Any>?) {
        // Firebase Analytics 이벤트 전송
        val bundle = Bundle()
        params?.forEach { (key, value) ->
            when (value) {
                is String -> bundle.putString(key, value)
                is Int -> bundle.putInt(key, value)
                is Long -> bundle.putLong(key, value)
                is Double -> bundle.putDouble(key, value)
                is Boolean -> bundle.putBoolean(key, value)
                else -> bundle.putString(key, value.toString())
            }
        }
        analytics.logEvent(name, bundle)
    }
}
