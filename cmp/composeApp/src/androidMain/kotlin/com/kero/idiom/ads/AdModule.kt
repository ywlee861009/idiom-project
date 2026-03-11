package com.kero.idiom.ads

import com.kero.idiom.MainActivity
import com.kero.idiom.core.ads.AdController
import org.koin.dsl.module

val androidAdModule = module {
    single<AdController> {
        AndroidAdController(
            context = get(),
            activityProvider = { MainActivity.currentActivity }
        )
    }
}
