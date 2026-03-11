package com.kero.idiom.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kero.idiom.data.local.dao.IdiomDao
import com.kero.idiom.data.local.dao.UserStatsDao
import com.kero.idiom.data.local.model.IdiomEntity
import com.kero.idiom.data.local.model.UserStatsEntity

@Database(
    entities = [IdiomEntity::class, UserStatsEntity::class],
    version = 1, // 정식 출시용 첫 번째 버전
    exportSchema = true
)
abstract class IdiomDatabase : RoomDatabase() {
    abstract fun idiomDao(): IdiomDao
    abstract fun userStatsDao(): UserStatsDao
}
