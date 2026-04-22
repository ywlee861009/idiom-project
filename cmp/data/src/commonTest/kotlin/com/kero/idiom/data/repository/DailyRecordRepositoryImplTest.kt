package com.kero.idiom.data.repository

import com.kero.idiom.data.local.model.DailyRecordEntity
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class DailyRecordRepositoryImplTest {

    private lateinit var realm: Realm
    private lateinit var repository: DailyRecordRepositoryImpl

    @BeforeTest
    fun setup() {
        val config = RealmConfiguration.Builder(
            schema = setOf(DailyRecordEntity::class)
        )
            .inMemory()
            .name("test-daily-record.realm")
            .build()
        realm = Realm.open(config)
        repository = DailyRecordRepositoryImpl(realm)
    }

    @AfterTest
    fun tearDown() {
        realm.close()
    }

    @Test
    fun recordToday_createsNewRecord() = runTest {
        repository.recordToday(solvedCount = 5, correctCount = 4, earnedXp = 12)

        val entities = realm.query<DailyRecordEntity>().find()
        assertEquals(1, entities.size)
        assertEquals(5, entities[0].solvedCount)
        assertEquals(4, entities[0].correctCount)
        assertEquals(12, entities[0].earnedXp)
    }

    @Test
    fun recordToday_calledTwice_accumulatesValues() = runTest {
        repository.recordToday(solvedCount = 5, correctCount = 4, earnedXp = 12)
        repository.recordToday(solvedCount = 5, correctCount = 3, earnedXp = 8)

        val entities = realm.query<DailyRecordEntity>().find()
        assertEquals(1, entities.size)
        assertEquals(10, entities[0].solvedCount)
        assertEquals(7, entities[0].correctCount)
        assertEquals(20, entities[0].earnedXp)
    }

    @Test
    fun getWeeklyRecords_returns7Days() = runTest {
        repository.recordToday(solvedCount = 5, correctCount = 5, earnedXp = 15)

        val records = repository.getWeeklyRecords()

        assertEquals(7, records.size)
        // 마지막 항목(오늘)에 데이터가 있어야 함
        val today = records.last()
        assertEquals(5, today.solvedCount)
        assertEquals(5, today.correctCount)
        assertEquals(15, today.earnedXp)
    }

    @Test
    fun getWeeklyRecords_emptyDaysHaveZeroValues() = runTest {
        val records = repository.getWeeklyRecords()

        assertEquals(7, records.size)
        records.forEach { record ->
            assertEquals(0, record.solvedCount)
            assertEquals(0, record.correctCount)
            assertEquals(0, record.earnedXp)
        }
    }

    @Test
    fun getWeeklyRecords_datesAreChronological() = runTest {
        val records = repository.getWeeklyRecords()

        assertEquals(7, records.size)
        for (i in 0 until records.size - 1) {
            assert(records[i].date < records[i + 1].date) {
                "Dates should be chronological: ${records[i].date} < ${records[i + 1].date}"
            }
        }
    }

    @Test
    fun recordToday_withZeroValues_stillCreatesRecord() = runTest {
        repository.recordToday(solvedCount = 0, correctCount = 0, earnedXp = 0)

        val entities = realm.query<DailyRecordEntity>().find()
        assertEquals(1, entities.size)
    }
}
