package com.kero.idiom.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kero.idiom.data.local.dao.IdiomDao
import com.kero.idiom.data.local.model.IdiomEntity

@Database(
    entities = [IdiomEntity::class],
    version = 2,
    exportSchema = false
)
abstract class IdiomDatabase : RoomDatabase() {
    abstract fun idiomDao(): IdiomDao
}
