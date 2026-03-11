package com.kero.idiom.core.util

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics

/**
 * 🌌 Android 전용 Logger 구현 (Logcat + Firebase)
 * 접두사 :::> 를 붙여 필터링이 용이하도록 설계했습니다.
 */
actual object Logger {
    private const val TAG = "IdiomQuiz"
    private const val PREFIX = ":::>"

    actual fun d(message: String) {
        Log.d(TAG, "$PREFIX $message")
    }

    actual fun e(message: String, throwable: Throwable?) {
        val fullMsg = "$PREFIX $message"
        Log.e(TAG, fullMsg, throwable)
        
        // 💡 오직 에러일 때만 Firebase Crashlytics에 로그 기록
        FirebaseCrashlytics.getInstance().log(fullMsg)
        throwable?.let {
            FirebaseCrashlytics.getInstance().recordException(it)
        }
    }
}
