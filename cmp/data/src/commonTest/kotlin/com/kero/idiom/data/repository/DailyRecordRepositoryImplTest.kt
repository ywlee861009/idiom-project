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

/** DailyRecordRepository의 Realm 구현체가 일일 기록 CRUD를 올바르게 수행하는지 검증 */
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

    /** 오늘 첫 기록 시 Realm에 새 엔티티가 생성되고 값이 정확한지 검증 */
    @Test
    fun recordToday_createsNewRecord() = runTest {
        repository.recordToday(solvedCount = 5, correctCount = 4, earnedXp = 12)

        val entities = realm.query<DailyRecordEntity>().find()
        assertEquals(1, entities.size)
        assertEquals(5, entities[0].solvedCount)
        assertEquals(4, entities[0].correctCount)
        assertEquals(12, entities[0].earnedXp)
    }

    /** 같은 날 두 번 기록 시 새 엔티티 생성이 아닌 기존 값에 누적되는지 검증 */
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

    /** 주간 조회 시 항상 7일치 데이터가 반환되고 오늘 데이터가 포함되는지 검증 */
    @Test
    fun getWeeklyRecords_returns7Days() = runTest {
        repository.recordToday(solvedCount = 5, correctCount = 5, earnedXp = 15)

        val records = repository.getWeeklyRecords()

        assertEquals(7, records.size)
        val today = records.last()
        assertEquals(5, today.solvedCount)
        assertEquals(5, today.correctCount)
        assertEquals(15, today.earnedXp)
    }

    /** 기록이 없는 날은 solvedCount/correctCount/earnedXp가 모두 0으로 채워지는지 검증 */
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

    /** 주간 기록의 날짜가 오래된 순서→최신 순서(시간순)로 정렬되는지 검증 */
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

    /** 모든 값이 0이어도 Realm 엔티티가 정상적으로 생성되는지 검증 (빈 세션 기록 허용) */
    @Test
    fun recordToday_withZeroValues_stillCreatesRecord() = runTest {
        repository.recordToday(solvedCount = 0, correctCount = 0, earnedXp = 0)

        val entities = realm.query<DailyRecordEntity>().find()
        assertEquals(1, entities.size)
    }
}
