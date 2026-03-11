package com.kero.idiom.core.logger

/**
 * 앱 전체에서 사용하는 통합 로거 인터페이스.
 * 플랫폼(Android/iOS)별로 구체적인 구현(Firebase 등)을 제공할 수 있도록 설계되었습니다.
 */
interface IdiomLogger {
    /**
     * Crashlytics 커스텀 로그를 남깁니다. (비정상 종료 시 리포트에 포함됨)
     */
    fun log(message: String)

    /**
     * 비정상 종료는 아니지만, 코드상에서 발생한 예외를 Crashlytics에 수동으로 기록합니다.
     */
    fun recordException(throwable: Throwable)

    /**
     * Analytics 이벤트를 전송합니다.
     * @param name 이벤트 이름
     * @param params 추가 정보 (Map 형태)
     */
    fun logEvent(name: String, params: Map<String, Any>? = null)
}
