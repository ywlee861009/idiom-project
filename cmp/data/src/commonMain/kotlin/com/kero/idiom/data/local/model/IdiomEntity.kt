package com.kero.idiom.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kero.idiom.domain.model.Idiom

/**
 * 사자성어 DB 엔티티.
 * [exposureCount] 필드를 통해 문제 출제 빈도를 관리합니다.
 */
@Entity(tableName = "idioms")
data class IdiomEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val word: String,
    val meaning: String,
    val hanja: String,
    val level: Int, // 난이도 (1: 상, 2: 중, 3: 하 등)
    val exposureCount: Int = 0, // 출제 빈도 (지능적 추출용)
    val correctCount: Int = 0  // 정답 횟수 (서고 획득 기준)
)

/**
 * 도메인 모델로 변환하는 확장 함수.
 */
fun IdiomEntity.toDomain(): Idiom = Idiom(
    word = word,
    meaning = meaning,
    hanja = hanja,
    level = level
)

/**
 * 도메인 모델에서 엔티티로 변환하는 확장 함수.
 */
fun Idiom.toEntity(): IdiomEntity = IdiomEntity(
    word = word,
    meaning = meaning,
    hanja = hanja,
    level = level
)
