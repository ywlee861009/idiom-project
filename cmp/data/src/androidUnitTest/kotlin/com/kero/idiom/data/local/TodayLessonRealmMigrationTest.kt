package com.kero.idiom.data.local

import com.kero.idiom.data.local.model.DailyRecordEntity
import com.kero.idiom.data.local.model.IdiomEntity
import com.kero.idiom.data.local.model.TodayLessonEntity
import com.kero.idiom.data.local.model.TodayLessonReviewEntity
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PersistedName
import io.realm.kotlin.types.annotations.PrimaryKey
import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull

class TodayLessonRealmMigrationTest {
    @Test
    fun versionThreePreservesVersionTwoCollectionAndStudyHistory() {
        val tempDirectory = Files.createTempDirectory("idiom-realm-migration").toFile()
        try {
            val currentConfig = RealmDatabase.config
            val versionTwo = RealmConfiguration.Builder(setOf(LegacyIdiomEntity::class, DailyRecordEntity::class))
                .directory(tempDirectory.path)
                .name(currentConfig.name)
                .schemaVersion(2)
                .build()
            val oldRealm = Realm.open(versionTwo)
            try {
                oldRealm.writeBlocking {
                    copyToRealm(LegacyIdiomEntity().apply {
                        id = "고진감래"
                        word = id
                        hanja = "苦盡甘來"
                        meaning = "고생 끝에 낙이 옴."
                        level = 1
                        exposureCount = 7
                        correctCount = 4
                    })
                    copyToRealm(DailyRecordEntity().apply {
                        date = "2026-11-05"
                        solvedCount = 5
                        correctCount = 4
                        earnedXp = 8
                    })
                }
            } finally {
                oldRealm.close()
            }

            val versionThree = RealmConfiguration.Builder(
                setOf(IdiomEntity::class, DailyRecordEntity::class, TodayLessonEntity::class, TodayLessonReviewEntity::class)
            )
                .directory(tempDirectory.path)
                .name(currentConfig.name)
                .schemaVersion(currentConfig.schemaVersion)
                .build()
            assertEquals(currentConfig.schema, versionThree.schema)
            val upgraded = Realm.open(versionThree)
            try {
                val idiom = assertNotNull(upgraded.query<IdiomEntity>().first().find())
                assertEquals("고진감래", idiom.word)
                assertEquals(7, idiom.exposureCount)
                assertEquals(4, idiom.correctCount)
                assertFalse(idiom.isArchived)
                assertEquals(8, upgraded.query<DailyRecordEntity>().first().find()?.earnedXp)
                assertEquals(0L, upgraded.query<TodayLessonEntity>().count().find())
                assertEquals(0L, upgraded.query<TodayLessonReviewEntity>().count().find())
            } finally {
                upgraded.close()
            }
        } finally {
            tempDirectory.deleteRecursively()
        }
    }
}

@PersistedName("IdiomEntity")
class LegacyIdiomEntity : RealmObject {
    @PrimaryKey
    var id: String = ""
    var word: String = ""
    var hanja: String = ""
    var meaning: String = ""
    var level: Int = 0
    var exposureCount: Int = 0
    var correctCount: Int = 0
}
