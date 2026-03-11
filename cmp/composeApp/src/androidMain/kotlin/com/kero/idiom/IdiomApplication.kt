package com.kero.idiom

import android.app.Application
import com.kero.idiom.ads.androidAdModule
import com.kero.idiom.data.di.dataModule
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
                androidAdModule
            )
        }
    }
}
