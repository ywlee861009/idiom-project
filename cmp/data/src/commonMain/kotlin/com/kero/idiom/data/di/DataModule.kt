package com.kero.idiom.data.di

import com.kero.idiom.data.datasource.AssetIdiomDataSource
import com.kero.idiom.data.local.IdiomDatabase
import com.kero.idiom.data.repository.IdiomRepositoryImpl
import com.kero.idiom.data.repository.UserStatsRepositoryImpl
import com.kero.idiom.domain.repository.IdiomRepository
import com.kero.idiom.domain.repository.UserStatsRepository
import org.koin.core.module.Module
import org.koin.dsl.module

internal expect val platformDataModule: Module

val dataModule = module {
    includes(platformDataModule)
    single { AssetIdiomDataSource() }
    single { get<IdiomDatabase>().idiomDao() }
    single { get<IdiomDatabase>().userStatsDao() }
    
    single<IdiomRepository> {
        IdiomRepositoryImpl(
            dataStore = get(),
            assetDataSource = get(),
            idiomDao = get()
        )
    }
    
    single<UserStatsRepository> {
        UserStatsRepositoryImpl(
            userStatsDao = get()
        )
    }
}
