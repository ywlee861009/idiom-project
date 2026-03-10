package com.kero.idiom.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.kero.idiom.data.datasource.AssetIdiomDataSource
import com.kero.idiom.data.local.IdiomDatabase
import com.kero.idiom.data.local.getRoomDatabase
import com.kero.idiom.data.repository.IdiomRepositoryImpl
import com.kero.idiom.domain.repository.IdiomRepository
import org.koin.core.module.Module
import org.koin.dsl.module

internal expect val platformDataModule: Module

val dataModule = module {
    includes(platformDataModule)
    single { AssetIdiomDataSource() }
    single { get<IdiomDatabase>().idiomDao() }
    single<IdiomRepository> {
        IdiomRepositoryImpl(
            dataStore = get(),
            assetDataSource = get(),
            idiomDao = get()
        )
    }
}
