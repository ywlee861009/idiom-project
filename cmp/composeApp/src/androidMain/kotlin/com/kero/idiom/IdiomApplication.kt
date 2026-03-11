package com.kero.idiom

import android.app.Application
import com.kero.idiom.ads.androidAdModule
import com.kero.idiom.core.logger.IdiomLog
import com.kero.idiom.core.logger.IdiomLogger
import com.kero.idiom.core.logger.androidLoggerModule
import com.kero.idiom.data.di.dataModule
import com.kero.idiom.domain.usecase.GetUserStatsUseCase
import com.kero.idiom.domain.usecase.UpdateUserStatsUseCase
import com.kero.idiom.feature.quiz.di.featureQuizModule
import com.kero.idiom.feature.result.di.featureResultModule
import com.kero.idiom.ui.profile.ProfileViewModel
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

class IdiomApplication : Application() {
    companion object {
        var instance: IdiomApplication? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        
        val appModule = module {
            // UseCases
            factory { GetUserStatsUseCase(get()) }
            factory { UpdateUserStatsUseCase(get()) }
            
            // ViewModels
            viewModel { ProfileViewModel(get()) }
        }

        startKoin {
            androidLogger()
            androidContext(this@IdiomApplication)
            modules(
                dataModule,
                appModule,
                featureQuizModule,
                featureResultModule,
                androidAdModule,
                androidLoggerModule
            )
        }

        // 전역 로거 식재 (Plant) - 이제 어디서든 IdiomLog 사용 가능!
        val logger: IdiomLogger by inject()
        IdiomLog.plant(logger)

        // 아주 깔끔한 한 줄 호출!
        IdiomLog.i("앱이 성공적으로 시작되었습니다. (IdiomLog 활용)")
        IdiomLog.event("app_start", mapOf("platform" to "Android"))
    }
}
