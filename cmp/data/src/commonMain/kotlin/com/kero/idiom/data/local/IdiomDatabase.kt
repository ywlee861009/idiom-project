package com.kero.idiom.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kero.idiom.data.local.dao.IdiomDao
import com.kero.idiom.data.local.dao.UserStatsDao
import com.kero.idiom.data.local.model.IdiomEntity
import com.kero.idiom.data.local.model.UserStatsEntity

@Database(
    entities = [IdiomEntity::class, UserStatsEntity::class],
    version = 4, // isNotificationEnabled 필드 추가로 인한 버전 상향
    exportSchema = false
)
abstract class IdiomDatabase : RoomDatabase() {
    abstract fun idiomDao(): IdiomDao
    abstract fun userStatsDao(): UserStatsDao
}
