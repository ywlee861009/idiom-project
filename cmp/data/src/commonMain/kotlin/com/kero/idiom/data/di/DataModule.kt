package com.kero.idiom.data.di

import com.kero.idiom.data.datasource.AssetIdiomDataSource
import com.kero.idiom.data.datasource.remote.RemoteIdiomDataSource
import com.kero.idiom.data.local.RealmDatabase
import com.kero.idiom.data.repository.IdiomRepositoryImpl
import com.kero.idiom.data.repository.UserStatsRepositoryImpl
import com.kero.idiom.domain.repository.IdiomRepository
import com.kero.idiom.domain.repository.UserStatsRepository
import io.realm.kotlin.Realm
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * 데이터 레이어 의존성 주입 모듈.
 * Room 설정을 제거하고 Realm 인스턴스를 주입하도록 변경했습니다.
 */
val dataModule = module {
    // DataSource
    singleOf(::AssetIdiomDataSource)
    singleOf(::RemoteIdiomDataSource)

    // Realm Database (Singleton)
    single { RealmDatabase.realm }

    // Repository implementation
    singleOf(::IdiomRepositoryImpl) bind IdiomRepository::class
    singleOf(::UserStatsRepositoryImpl) bind UserStatsRepository::class
}
