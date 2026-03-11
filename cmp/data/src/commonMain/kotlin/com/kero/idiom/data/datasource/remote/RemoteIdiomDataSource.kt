package com.kero.idiom.data.datasource.remote

import com.kero.idiom.core.util.Logger
import com.kero.idiom.domain.model.Idiom
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.Serializable

@Serializable
data class IdiomVersion(val version: Int)

class RemoteIdiomDataSource(
    private val httpClient: HttpClient
) {
    private val GITHUB_BASE_URL = "https://raw.githubusercontent.com/ywlee861009/idiom-project/main/assets"

    /**
     * GitHub에서 version.json을 읽어와 현재 데이터 버전을 확인합니다.
     */
    suspend fun getRemoteVersion(): Int? {
        return try {
            val response: IdiomVersion = httpClient.get("$GITHUB_BASE_URL/version.json").body()
            Logger.d("Remote version fetch success: ${response.version}")
            response.version
        } catch (e: Exception) {
            // 💡 여기서 왜 실패하는지 로그를 찍어야 합니다!
            Logger.e("Remote version check failed: ${e.message}", e)
            null
        }
    }

    /**
     * GitHub에서 idioms.json을 읽어와 사자성어 리스트를 반환합니다.
     */
    suspend fun getRemoteIdioms(): List<Idiom> {
        return try {
            val response: List<Idiom> = httpClient.get("$GITHUB_BASE_URL/idioms.json").body()
            Logger.d("Remote idioms fetch success: ${response.size} items")
            response
        } catch (e: Exception) {
            Logger.e("Remote idioms fetch failed: ${e.message}", e)
            emptyList()
        }
    }
}
