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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class IdiomRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
    private val assetDataSource: AssetIdiomDataSource,
    private val remoteDataSource: RemoteIdiomDataSource,
    private val idiomDao: IdiomDao
) : IdiomRepository {

    private val IDIOM_VERSION = intPreferencesKey("idiom_version")

    override suspend fun syncIfNeeded(onStatusChanged: ((String) -> Unit)?) {
        onStatusChanged?.invoke("서책을 정리 중입니다...")
        
        val storedVersion = dataStore.data.map { it[IDIOM_VERSION] ?: 0 }.first()
        val remoteVersion = remoteDataSource.getRemoteVersion() ?: 0

        com.kero.idiom.core.util.Logger.d("Data Sync: [Stored: v$storedVersion] vs [Remote: v$remoteVersion]")

        // 💡 새로운 버전 발견 또는 DB가 비어있는 경우
        if (remoteVersion > storedVersion || idiomDao.getCount() == 0) {
            
            if (remoteVersion > storedVersion) {
                onStatusChanged?.invoke("새로운 서책 목록이 발견되었습니다!")
                delay(1000) // 유저가 읽을 시간을 줌
            }

            onStatusChanged?.invoke("서책을 업데이트 중입니다...")
            delay(500)

            val remoteIdioms = remoteDataSource.getRemoteIdioms()
            
            if (remoteIdioms.isNotEmpty()) {
                idiomDao.deleteAll()
                idiomDao.insertAll(remoteIdioms.map { it.toEntity() })
                dataStore.edit { it[IDIOM_VERSION] = remoteVersion }
                onStatusChanged?.invoke("서책 업데이트 완료!")
                delay(500)
            } else if (idiomDao.getCount() == 0) {
                // 서버 실패 시 로컬 Asset으로 Fallback
                syncFromLocalAsset(onStatusChanged)
            }
        }
    }

    private suspend fun syncFromLocalAsset(onStatusChanged: ((String) -> Unit)?) {
        onStatusChanged?.invoke("로컬 서책을 불러오는 중입니다...")
        val idioms = assetDataSource.getIdiomsFromAssets()
        if (idioms.isNotEmpty()) {
            idiomDao.deleteAll()
            idiomDao.insertAll(idioms.map { it.toEntity() })
            dataStore.edit { it[IDIOM_VERSION] = 0 }
            onStatusChanged?.invoke("서책 준비 완료!")
            delay(500)
        }
    }

    override suspend fun getRandomIdioms(limit: Int): List<Idiom> {
        // syncIfNeeded()를 콜백 없이 호출 (기본값)
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
