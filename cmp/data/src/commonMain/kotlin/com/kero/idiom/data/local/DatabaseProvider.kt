package com.kero.idiom.data.local

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

fun getRoomDatabase(
    builder: RoomDatabase.Builder<IdiomDatabase>
): IdiomDatabase {
    return builder
        .setDriver(BundledSQLiteDriver()) // KMP에서 권장하는 번들 드라이버
        .setQueryCoroutineContext(Dispatchers.IO)
        .fallbackToDestructiveMigration(true)
        .build()
}

expect fun getDatabaseBuilder(): RoomDatabase.Builder<IdiomDatabase>
