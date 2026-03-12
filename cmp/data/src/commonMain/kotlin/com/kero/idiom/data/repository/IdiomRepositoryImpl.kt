package com.kero.idiom.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.kero.idiom.core.util.Logger
import com.kero.idiom.data.datasource.AssetIdiomDataSource
import com.kero.idiom.data.datasource.remote.RemoteIdiomDataSource
import com.kero.idiom.data.local.model.IdiomEntity
import com.kero.idiom.data.local.model.toDomain
import com.kero.idiom.data.local.model.toEntity
import com.kero.idiom.domain.model.Idiom
import com.kero.idiom.domain.repository.IdiomRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Realm을 사용한 성어 데이터 저장소 구현체.
 */
class IdiomRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
    private val assetDataSource: AssetIdiomDataSource,
    private val remoteDataSource: RemoteIdiomDataSource,
    private val realm: Realm // Room Dao 대신 Realm 인스턴스 주입
) : IdiomRepository {

    private val IDIOM_VERSION = intPreferencesKey("user_data_version")

    override suspend fun syncIfNeeded(onStatusChanged: ((String) -> Unit)?) {
        onStatusChanged?.invoke("서책을 정리 중입니다...")
        
        val storedVersion = dataStore.data.map { it[IDIOM_VERSION] ?: 0 }.first()
        val remoteVersion = remoteDataSource.getRemoteVersion() ?: 0
        val currentCount = realm.query<IdiomEntity>().count().find()

        Logger.d("Data Sync Check: [Stored: v$storedVersion] [Remote: v$remoteVersion] [DB Count: $currentCount]")

        // 💡 새로운 버전 발견 또는 DB가 비어있는 경우
        if (remoteVersion > storedVersion || currentCount == 0L) {
            
            if (remoteVersion > storedVersion) {
                Logger.d("New version found! v$storedVersion -> v$remoteVersion")
                onStatusChanged?.invoke("새로운 서책 목록이 발견되었습니다!")
                delay(1000)
            } else {
                Logger.d("Database is empty. Initializing sync...")
            }

            onStatusChanged?.invoke("서책을 업데이트 중입니다...")
            delay(500)

            val remoteIdioms = remoteDataSource.getRemoteIdioms()
            
            if (remoteIdioms.isNotEmpty()) {
                Logger.d("Syncing ${remoteIdioms.size} idioms from remote...")
                realm.write {
                    val all = query<IdiomEntity>().find()
                    delete(all) // 기존 데이터 초기화 (deleteAll() 대신 명시적 삭제)
                    remoteIdioms.forEach { copyToRealm(it.toEntity()) }
                }
                dataStore.edit { it[IDIOM_VERSION] = remoteVersion }
                onStatusChanged?.invoke("서책 업데이트 완료!")
                Logger.d("Sync finished: v$remoteVersion stored.")
                delay(500)
            } else if (currentCount == 0L) {
                Logger.d("Remote fetch failed. Falling back to local asset...")
                syncFromLocalAsset(onStatusChanged)
            }
        } else {
            Logger.d("Data is already up to date. (v$storedVersion)")
            onStatusChanged?.invoke("서책 준비 완료!")
            delay(500)
        }
    }

    private suspend fun syncFromLocalAsset(onStatusChanged: ((String) -> Unit)?) {
        onStatusChanged?.invoke("로컬 서책을 불러오는 중입니다...")
        val idioms = assetDataSource.getIdiomsFromAssets()
        if (idioms.isNotEmpty()) {
            Logger.d("Syncing ${idioms.size} idioms from assets...")
            realm.write {
                val all = query<IdiomEntity>().find()
                delete(all)
                idioms.forEach { copyToRealm(it.toEntity()) }
            }
            dataStore.edit { it[IDIOM_VERSION] = 0 }
            onStatusChanged?.invoke("서책 준비 완료!")
            delay(500)
        }
    }

    override suspend fun getRandomIdioms(limit: Int): List<Idiom> {
        // [Smart Revisit] 노출 빈도가 적은 순서로 정렬 후 랜덤 추출
        return realm.query<IdiomEntity>()
            .find()
            .sortedBy { it.exposureCount } // 노출 적은 순
            .take(limit * 3) // 여유있게 가져와서
            .shuffled() // 섞고
            .take(limit) // 최종 개수 추출
            .map { it.toDomain() }
    }

    override suspend fun recordExposure(word: String) {
        realm.write {
            val idiom = query<IdiomEntity>("word == $0", word).first().find()
            idiom?.exposureCount = (idiom?.exposureCount ?: 0) + 1
        }
    }

    override suspend fun recordCorrectAnswer(word: String) {
        realm.write {
            val idiom = query<IdiomEntity>("word == $0", word).first().find()
            idiom?.correctCount = (idiom?.correctCount ?: 0) + 1
        }
    }

    override suspend fun getAllIdioms(): List<Idiom> {
        return realm.query<IdiomEntity>().find().map { it.toDomain() }
    }

    override suspend fun getAcquiredIdioms(): List<Idiom> {
        return realm.query<IdiomEntity>("correctCount > 0").find()
            .sortedBy { it.word }
            .map { it.toDomain() }
    }
}
