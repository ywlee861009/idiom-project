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
                    val remoteWords = remoteIdioms.map { it.word }.toSet()
                    
                    // 1. 서버에 없는 데이터 삭제 (선택적)
                    val allLocal = query<IdiomEntity>().find()
                    allLocal.forEach { local ->
                        if (!remoteWords.contains(local.word)) {
                            delete(local)
                        }
                    }

                    // 2. 스마트 병합: 기존 데이터는 보존하고 정보만 업데이트
                    remoteIdioms.forEach { remote ->
                        val existing = query<IdiomEntity>("word == $0", remote.word).first().find()
                        if (existing != null) {
                            // 메타데이터만 업데이트하고 진행상황(count)은 유지
                            existing.meaning = remote.meaning
                            existing.hanja = remote.hanja
                            existing.level = remote.level
                        } else {
                            // 신규 성어인 경우에만 새로 추가
                            copyToRealm(remote.toEntity())
                        }
                    }
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
                idioms.forEach { remote ->
                    val existing = query<IdiomEntity>("word == $0", remote.word).first().find()
                    if (existing != null) {
                        existing.meaning = remote.meaning
                        existing.hanja = remote.hanja
                        existing.level = remote.level
                    } else {
                        copyToRealm(remote.toEntity())
                    }
                }
            }
            dataStore.edit { it[IDIOM_VERSION] = 0 }
            onStatusChanged?.invoke("서책 준비 완료!")
            delay(500)
        }
    }

    override suspend fun getRandomIdioms(limit: Int): List<Idiom> {
        // [Smart Revisit] 노출 빈도가 적고 정답률이 낮은 순서로 정렬 후 랜덤 추출
        // poolSize를 limit * 10 (최소 30개)으로 늘려 중복 노출 패턴을 깨트림
        val poolSize = (limit * 10).coerceAtLeast(30)
        
        return realm.query<IdiomEntity>()
            .find()
            .sortedWith(
                compareBy<IdiomEntity> { it.exposureCount }
                    .thenBy { it.correctCount } // 미수집(정답 0회) 성어 우선
            )
            .take(poolSize) // 넉넉한 풀에서 가져와서
            .shuffled() // 완전히 섞고
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
