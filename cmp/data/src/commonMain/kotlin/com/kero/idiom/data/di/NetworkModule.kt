package com.kero.idiom.data.di

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module

/**
 * Ktor HttpClient 설정을 관리하는 네트워크 모듈.
 * GitHub Raw URL이 text/plain을 반환하므로 해당 타입도 JSON으로 처리하도록 설정합니다.
 */
val networkModule = module {
    single {
        val jsonInstance = Json {
            ignoreUnknownKeys = true
            prettyPrint = true
            isLenient = true
        }

        HttpClient {
            install(ContentNegotiation) {
                // 기본 application/json 처리
                json(jsonInstance)
                
                // 💡 GitHub Raw 파일 대응: text/plain으로 오는 데이터도 JSON으로 변환하도록 등록
                register(ContentType.Text.Plain, KotlinxSerializationConverter(jsonInstance))
                register(ContentType.Application.OctetStream, KotlinxSerializationConverter(jsonInstance))
            }
            
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.INFO
            }
        }
    }
}
