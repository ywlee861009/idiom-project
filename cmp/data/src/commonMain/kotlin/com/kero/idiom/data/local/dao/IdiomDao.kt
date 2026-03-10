package com.kero.idiom.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kero.idiom.data.local.model.IdiomEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IdiomDao {
    /**
     * 전체 사자성어 개수 확인 (초기 동기화 여부 판단용)
     */
    @Query("SELECT COUNT(*) FROM idioms")
    suspend fun getCount(): Int

    /**
     * DB 초기화 (버전 업데이트 시 사용)
     */
    @Query("DELETE FROM idioms")
    suspend fun deleteAll()

    /**
     * 사자성어 대량 삽입 (초기 동기화 시 사용)
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(idioms: List<IdiomEntity>)

    /**
     * 모든 사자성어 조회
     */
    @Query("SELECT * FROM idioms")
    fun getAllIdioms(): Flow<List<IdiomEntity>>

    /**
     * [Smart Revisit Algorithm]
     * 출제 빈도가 적은 순서(exposureCount ASC)로 정렬 후,
     * 랜덤하게 [limit]개 추출.
     */
    @Query("SELECT * FROM idioms ORDER BY exposureCount ASC, RANDOM() LIMIT :limit")
    suspend fun getRandomIdioms(limit: Int = 10): List<IdiomEntity>

    /**
     * 출제 횟수 증가 (ID-21 핵심 기능)
     */
    @Query("UPDATE idioms SET exposureCount = exposureCount + 1 WHERE word = :word")
    suspend fun incrementExposureCount(word: String)

    /**
     * 정답 횟수 증가 (서고 획득 기준)
     */
    @Query("UPDATE idioms SET correctCount = correctCount + 1 WHERE word = :word")
    suspend fun incrementCorrectCount(word: String)

    /**
     * 정답을 한 번이라도 맞힌 성어만 반환 (서고 획득 목록)
     */
    @Query("SELECT * FROM idioms WHERE correctCount > 0 ORDER BY word ASC")
    suspend fun getAcquiredIdioms(): List<IdiomEntity>
}
