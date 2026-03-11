package com.kero.idiom.core.logger

/**
 * 앱 어디서든 주입 없이 로깅을 할 수 있도록 도와주는 전역 객체.
 * Timber 라이브러리와 유사한 방식으로 작동합니다.
 */
object IdiomLog {
    private var logger: IdiomLogger? = null

    /**
     * 사용할 로거 구현체를 등록합니다. (보통 Application onCreate에서 호출)
     */
    fun plant(logger: IdiomLogger) {
        this.logger = logger
    }

    /**
     * 정보성 로그를 남깁니다. (Crashlytics 로그에 포함됨)
     */
    fun i(message: String) {
        logger?.log(message)
    }

    /**
     * 예외 상황을 기록합니다.
     */
    fun e(throwable: Throwable) {
        logger?.recordException(throwable)
    }

    /**
     * Analytics 이벤트를 전송합니다.
     */
    fun event(name: String, params: Map<String, Any>? = null) {
        logger?.logEvent(name, params)
    }
}
