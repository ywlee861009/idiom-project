package com.kero.idiom.data.di

import com.kero.idiom.data.datasource.AssetIdiomDataSource
import com.kero.idiom.data.datasource.remote.RemoteIdiomDataSource
import com.kero.idiom.data.local.IdiomDatabase
import com.kero.idiom.data.repository.IdiomRepositoryImpl
import com.kero.idiom.data.repository.UserStatsRepositoryImpl
import com.kero.idiom.domain.repository.IdiomRepository
import com.kero.idiom.domain.repository.UserStatsRepository
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.dsl.module

internal expect val platformDataModule: Module

val dataModule = module {
    includes(platformDataModule)

    // Ktor HttpClient 공통 설정
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                }, contentType = io.ktor.http.ContentType.Any) // 💡 GitHub Raw(text/plain) 대응
            }
            install(Logging) {
                logger = object : io.ktor.client.plugins.logging.Logger {
                    override fun log(message: String) {
                        com.kero.idiom.core.util.Logger.d("Network: $message")
                    }
                }
                level = LogLevel.ALL
            }
        }
    }

    single { AssetIdiomDataSource() }
    single { RemoteIdiomDataSource(get()) }
    single { get<IdiomDatabase>().idiomDao() }
    single { get<IdiomDatabase>().userStatsDao() }
    
    single<IdiomRepository> {
        IdiomRepositoryImpl(
            dataStore = get(),
            assetDataSource = get(),
            remoteDataSource = get(),
            idiomDao = get()
        )
    }
    
    single<UserStatsRepository> {
        UserStatsRepositoryImpl(
            userStatsDao = get(),
            dataStore = get()
        )
    }
}
