package com.kero.idiom.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.kero.idiom.data.datasource.AssetIdiomDataSource
import com.kero.idiom.data.local.dao.IdiomDao
import com.kero.idiom.data.local.model.toDomain
import com.kero.idiom.data.local.model.toEntity
import com.kero.idiom.domain.model.Idiom
import com.kero.idiom.domain.repository.IdiomRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "settings")

@Singleton
class IdiomRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val assetDataSource: AssetIdiomDataSource,
    private val idiomDao: IdiomDao
) : IdiomRepository {

    companion object {
        private const val CURRENT_DATA_VERSION = 1 // 여기서 데이터 버전을 관리합니다.
    }

    private val IDIOM_VERSION = intPreferencesKey("idiom_version")

    /**
     * 초기 동기화 (Source-Controlled Migration)
     * 코드의 데이터 버전이 앱에 저장된 버전보다 높으면 DB를 날리고 새로 넣습니다.
     */
    override suspend fun syncIfNeeded() {
        val storedVersion = context.dataStore.data.map { it[IDIOM_VERSION] ?: 0 }.first()

        if (storedVersion < CURRENT_DATA_VERSION || idiomDao.getCount() == 0) {
            // 1. 기존 DB 데이터 전체 삭제 (마크님 요청 사항)
            idiomDao.deleteAll()

            // 2. JSON 데이터 다시 로드 및 삽입
            val idioms = assetDataSource.getIdiomsFromAssets()
            val entities = idioms.map { it.toEntity() }
            idiomDao.insertAll(entities)
            
            // 3. 현재 버전 저장
            context.dataStore.edit { it[IDIOM_VERSION] = CURRENT_DATA_VERSION }
        }
    }

    /**
     * [Smart Revisit Algorithm]
     * 출제 빈도가 적은 사자성어를 우선적으로 10개 추출합니다.
     */
    override suspend fun getRandomIdioms(limit: Int): List<Idiom> {
        // 동기화 보장 (필요시 호출)
        syncIfNeeded()
        
        return idiomDao.getRandomIdioms(limit).map { it.toDomain() }
    }

    /**
     * 출제 횟수 증가 (ID-21 핵심 기능)
     * 문제가 출제되었을 때 호출하여 exposureCount를 올립니다.
     */
    override suspend fun recordExposure(word: String) {
        idiomDao.incrementExposureCount(word)
    }
}
