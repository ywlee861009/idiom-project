package com.kero.idiom.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.kero.idiom.data.local.getAndroidDatabaseBuilder
import com.kero.idiom.data.local.getRoomDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "idiom_settings")

actual val platformDataModule: Module = module {
    single { 
        getRoomDatabase(getAndroidDatabaseBuilder(androidContext()))
    }
    
    single { 
        androidContext().dataStore
    }
}
