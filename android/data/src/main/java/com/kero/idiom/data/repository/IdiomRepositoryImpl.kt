package com.kero.idiom.data.repository

import com.kero.idiom.data.datasource.AssetIdiomDataSource
import com.kero.idiom.domain.model.Idiom
import com.kero.idiom.domain.repository.IdiomRepository
import javax.inject.Inject
import javax.inject.Singleton

class IdiomRepositoryImpl @Inject constructor(
    private val dataSource: AssetIdiomDataSource
) : IdiomRepository {

    private var cachedIdioms: List<Idiom> = emptyList()

    override suspend fun getIdioms(): List<Idiom> {
        if (cachedIdioms.isEmpty()) {
            cachedIdioms = dataSource.getIdiomsFromAssets()
        }
        return cachedIdioms
    }

    override suspend fun getRandomIdiom(): Idiom {
        return getIdioms().random()
    }
}
