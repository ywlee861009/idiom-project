package com.kero.idiom.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import okio.Path.Companion.toPath

/**
 * Compose Multiplatform용 DataStore 생성 팩토리.
 * 플랫폼별로 다른 파일 경로를 받아와서 DataStore 인스턴스를 생성합니다.
 */
fun createDataStore(producePath: () -> String): DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath(
        produceFile = { producePath().toPath() }
    )
}

internal const val DATASTORE_FILE_NAME = "idiom.preferences_pb"
