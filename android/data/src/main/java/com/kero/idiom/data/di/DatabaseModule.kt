package com.kero.idiom.data.di

import android.content.Context
import androidx.room.Room
import com.kero.idiom.data.local.IdiomDatabase
import com.kero.idiom.data.local.dao.IdiomDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): IdiomDatabase {
        return Room.databaseBuilder(
            context,
            IdiomDatabase::class.java,
            "idiom_database"
        ).build()
    }

    @Provides
    fun provideIdiomDao(database: IdiomDatabase): IdiomDao {
        return database.idiomDao()
    }
}
