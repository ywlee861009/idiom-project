package com.kero.idiom.core.logger

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Android 전용 로거 주입 모듈.
 */
val androidLoggerModule = module {
    // IdiomLogger 인터페이스에 FirebaseLogger를 싱글톤으로 주입
    single<IdiomLogger> { FirebaseLogger(androidContext()) }
}
