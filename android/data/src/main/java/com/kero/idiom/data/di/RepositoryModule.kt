package com.kero.idiom.data.di

import com.kero.idiom.data.repository.IdiomRepositoryImpl
import com.kero.idiom.domain.repository.IdiomRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindIdiomRepository(
        idiomRepositoryImpl: IdiomRepositoryImpl
    ): IdiomRepository
}
