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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

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

    private val localMutex = Mutex()
    private val remoteMutex = Mutex()

    override suspend fun initializeLocal() = localMutex.withLock {
        if (realm.query<IdiomEntity>("isArchived == false").count().find() > 0L) return@withLock

        val bundledIdioms = assetDataSource.getIdiomsFromAssets()
        check(bundledIdioms.isNotEmpty()) { "The bundled idiom catalog is empty." }
        dataStore.edit { it.remove(IDIOM_VERSION) }
        mergeCatalog(bundledIdioms)
    }

    override suspend fun syncIfNeeded(onStatusChanged: ((String) -> Unit)?) {
        remoteMutex.withLock {
            initializeLocal()
            val storedVersion = dataStore.data.first()[IDIOM_VERSION] ?: 0
            val remoteVersion = remoteDataSource.getRemoteVersion() ?: return@withLock
            if (remoteVersion <= storedVersion) return@withLock

            onStatusChanged?.invoke("서책을 업데이트 중입니다...")
            val remoteIdioms = remoteDataSource.getRemoteIdioms()
            if (remoteIdioms.isEmpty()) return@withLock

            mergeCatalog(remoteIdioms)
            dataStore.edit { it[IDIOM_VERSION] = remoteVersion }
            onStatusChanged?.invoke("서책 업데이트 완료!")
            Logger.d("Sync finished: v$remoteVersion stored.")
        }
    }

    private suspend fun mergeCatalog(incoming: List<Idiom>) {
        realm.write {
            val existingByWord = query<IdiomEntity>().find().associateBy { it.word }
            val incomingWords = incoming.mapTo(mutableSetOf()) { it.word }
            // Keep historical acquisitions and in-flight answers when the catalog changes.
            existingByWord.values.forEach { it.isArchived = it.word !in incomingWords }

            incoming.distinctBy { it.word }.forEach { updated ->
                val existing = existingByWord[updated.word]
                if (existing == null) {
                    copyToRealm(updated.toEntity())
                } else {
                    existing.meaning = updated.meaning
                    existing.hanja = updated.hanja
                    existing.level = updated.level
                }
            }
        }
    }

    override suspend fun getRandomIdioms(limit: Int): List<Idiom> {
        // [Smart Revisit] 노출 빈도가 적고 정답률이 낮은 순서로 정렬 후 랜덤 추출
        // poolSize를 limit * 10 (최소 30개)으로 늘려 중복 노출 패턴을 깨트림
        val poolSize = (limit * 10).coerceAtLeast(30)
        
        return realm.query<IdiomEntity>("isArchived == false")
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
        return realm.query<IdiomEntity>("isArchived == false OR correctCount > 0").find().map { it.toDomain() }
    }

    override suspend fun getAcquiredIdioms(): List<Idiom> {
        return realm.query<IdiomEntity>("correctCount > 0").find()
            .sortedBy { it.word }
            .map { it.toDomain() }
    }
}
