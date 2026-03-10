package com.kero.idiom.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.kero.idiom.data.datasource.AssetIdiomDataSource
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
    private val idiomDao: IdiomDao
) : IdiomRepository {

    companion object {
        private const val CURRENT_DATA_VERSION = 2
    }

    private val IDIOM_VERSION = intPreferencesKey("idiom_version")

    override suspend fun syncIfNeeded() {
        val storedVersion = dataStore.data.map { it[IDIOM_VERSION] ?: 0 }.first()

        if (storedVersion < CURRENT_DATA_VERSION || idiomDao.getCount() == 0) {
            idiomDao.deleteAll()

            val idioms = assetDataSource.getIdiomsFromAssets()
            val entities = idioms.map { it.toEntity() }
            idiomDao.insertAll(entities)
            
            dataStore.edit { it[IDIOM_VERSION] = CURRENT_DATA_VERSION }
        }
    }

    override suspend fun getRandomIdioms(limit: Int): List<Idiom> {
        syncIfNeeded()
        return idiomDao.getRandomIdioms(limit).map { it.toDomain() }
    }

    override suspend fun recordExposure(word: String) {
        idiomDao.incrementExposureCount(word)
    }
}
