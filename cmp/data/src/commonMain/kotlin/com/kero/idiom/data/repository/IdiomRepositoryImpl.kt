package com.kero.idiom.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.kero.idiom.data.datasource.AssetIdiomDataSource
import com.kero.idiom.data.datasource.remote.RemoteIdiomDataSource
import com.kero.idiom.data.local.dao.IdiomDao
import com.kero.idiom.data.local.model.toDomain
import com.kero.idiom.data.local.model.toEntity
import com.kero.idiom.domain.model.Idiom
import com.kero.idiom.domain.repository.IdiomRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class IdiomRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
    private val assetDataSource: AssetIdiomDataSource,
    private val remoteDataSource: RemoteIdiomDataSource,
    private val idiomDao: IdiomDao
) : IdiomRepository {

    private val IDIOM_VERSION = intPreferencesKey("idiom_version")

    /**
     * GitHub 기반 서버 동기화 로직
     * 1. 서버의 version.json 확인
     * 2. 로컬 버전보다 높거나 Room이 비어있으면 idioms.json 다운로드
     * 3. 실패 시 로컬 Asset으로 Fallback
     */
    override suspend fun syncIfNeeded() {
        val storedVersion = dataStore.data.map { it[IDIOM_VERSION] ?: 0 }.first()
        val remoteVersion = remoteDataSource.getRemoteVersion() ?: 0

        // 1. 서버에 새로운 버전이 있거나, DB가 비어있는 경우
        if (remoteVersion > storedVersion || idiomDao.getCount() == 0) {
            val remoteIdioms = remoteDataSource.getRemoteIdioms()
            
            if (remoteIdioms.isNotEmpty()) {
                // 서버에서 데이터를 성공적으로 받아온 경우
                idiomDao.deleteAll()
                idiomDao.insertAll(remoteIdioms.map { it.toEntity() })
                dataStore.edit { it[IDIOM_VERSION] = remoteVersion }
                println("✅ Sync success: Version $remoteVersion from Remote")
            } else if (idiomDao.getCount() == 0) {
                // 서버 통신 실패했는데 DB가 아예 비어있으면 Local Asset 사용
                syncFromLocalAsset()
            }
        }
    }

    private suspend fun syncFromLocalAsset() {
        val idioms = assetDataSource.getIdiomsFromAssets()
        if (idioms.isNotEmpty()) {
            idiomDao.deleteAll()
            idiomDao.insertAll(idioms.map { it.toEntity() })
            // Asset 버전은 최소값(0)으로 일단 세팅하여 차후 서버 업데이트 가능하게 함
            dataStore.edit { it[IDIOM_VERSION] = 0 }
            println("📂 Sync: Loaded from Local Asset (Fallback)")
        }
    }

    override suspend fun getRandomIdioms(limit: Int): List<Idiom> {
        // getRandomIdioms 호출 시마다 동기화 여부 체크 (필요시 실행됨)
        syncIfNeeded()
        return idiomDao.getRandomIdioms(limit).map { it.toDomain() }
    }

    override suspend fun recordExposure(word: String) {
        idiomDao.incrementExposureCount(word)
    }

    override suspend fun recordCorrectAnswer(word: String) {
        idiomDao.incrementCorrectCount(word)
    }

    override suspend fun getAllIdioms(): List<Idiom> {
        return idiomDao.getAllIdioms().map { entities -> entities.map { it.toDomain() } }.first()
    }

    override suspend fun getAcquiredIdioms(): List<Idiom> {
        return idiomDao.getAcquiredIdioms().map { it.toDomain() }
    }
}
