package com.kero.idiom.core.util

/**
 * 🌌 공통 로깅 모듈 (Compose Multiplatform)
 * :::> 접두사를 사용하여 Logcat에서 필터링 가능하게 설계되었습니다.
 */
expect object Logger {
    /**
     * 디버그 로그 출력 (Logcat)
     */
    fun d(message: String)

    /**
     * 에러 로그 출력 (Logcat + Firebase Crashlytics)
     */
    fun e(message: String, throwable: Throwable? = null)
}
