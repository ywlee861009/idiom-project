package com.kero.idiom.data.datasource.remote

import com.kero.idiom.domain.model.Idiom
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class IdiomVersion(val version: Int)

class RemoteIdiomDataSource(
    private val httpClient: HttpClient
) {
    // 💡 사장님의 저장소 정보로 교체 필요! (일단 더미 URL로 설정)
    private val GITHUB_BASE_URL = "https://raw.githubusercontent.com/ywlee861009/idiom-project/main/assets"

    /**
     * GitHub에서 version.json을 읽어와 현재 데이터 버전을 확인합니다.
     */
    suspend fun getRemoteVersion(): Int? {
        return try {
            val response: IdiomVersion = httpClient.get("$GITHUB_BASE_URL/version.json").body()
            response.version
        } catch (e: Exception) {
            println("❌ Remote version check failed: ${e.message}")
            null
        }
    }

    /**
     * GitHub에서 idioms.json을 읽어와 사자성어 리스트를 반환합니다.
     */
    suspend fun getRemoteIdioms(): List<Idiom> {
        return try {
            httpClient.get("$GITHUB_BASE_URL/idioms.json").body()
        } catch (e: Exception) {
            println("❌ Remote idioms fetch failed: ${e.message}")
            emptyList()
        }
    }
}
