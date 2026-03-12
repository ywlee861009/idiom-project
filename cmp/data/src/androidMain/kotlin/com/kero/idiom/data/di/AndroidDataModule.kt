package com.kero.idiom.data.di

import android.content.Context
import com.kero.idiom.data.local.DATASTORE_FILE_NAME
import com.kero.idiom.data.local.createDataStore
import org.koin.dsl.module

/**
 * 안드로이드 전용 데이터 의존성 모듈.
 * DataStore 등 컨텍스트가 필요한 객체들을 생성합니다.
 */
val androidDataModule = module {
    single {
        val context: Context = get()
        createDataStore {
            context.filesDir.resolve(DATASTORE_FILE_NAME).absolutePath
        }
    }
}
