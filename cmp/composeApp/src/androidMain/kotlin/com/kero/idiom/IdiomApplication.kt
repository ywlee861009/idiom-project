package com.kero.idiom

import android.app.Application
import com.kero.idiom.ads.androidAdModule
import com.kero.idiom.core.util.Logger
import com.kero.idiom.data.di.androidDataModule
import com.kero.idiom.data.di.dataModule
import com.kero.idiom.data.di.networkModule
import com.kero.idiom.domain.usecase.GetUserStatsUseCase
import com.kero.idiom.domain.usecase.UpdateUserStatsUseCase
import com.kero.idiom.feature.quiz.di.featureQuizModule
import com.kero.idiom.feature.result.di.featureResultModule
import com.kero.idiom.ui.profile.ProfileViewModel
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
            viewModel { ProfileViewModel(get(), get()) }
        }

        startKoin {
            androidLogger()
            androidContext(this@IdiomApplication)
            modules(
                dataModule,
                androidDataModule,
                networkModule,
                appModule,
                featureQuizModule,
                featureResultModule,
                androidAdModule
            )
        }

        // 🌌 새로 만든 공통 Logger 사용
        Logger.d("앱이 성공적으로 시작되었습니다. (:::> HttpClient 주입 완료)")
    }
}
