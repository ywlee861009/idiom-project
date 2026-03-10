package com.kero.idiom

import android.app.Application
import com.kero.idiom.data.di.dataModule
import com.kero.idiom.feature.quiz.di.featureQuizModule
import com.kero.idiom.feature.result.di.featureResultModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module

class IdiomApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidLogger()
            androidContext(this@IdiomApplication)
            modules(
                dataModule,
                featureQuizModule,
                featureResultModule
            )
        }
    }
}
