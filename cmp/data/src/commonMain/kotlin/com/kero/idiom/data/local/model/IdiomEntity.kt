package com.kero.idiom.data.local.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import com.kero.idiom.domain.model.Idiom

/**
 * 사자성어 Realm 데이터 객체.
 * [exposureCount] 필드를 통해 문제 출제 빈도를 관리합니다.
 * Realm 사용을 위해 var 필드와 기본 생성자가 필요합니다.
 */
class IdiomEntity : RealmObject {
    @PrimaryKey
    var id: String = "" // Realm에서는 String 또는 ObjectId PK를 선호함 (중복 방지)
    var word: String = ""
    var meaning: String = ""
    var hanja: String = ""
    var level: Int = 0
    var exposureCount: Int = 0
    var correctCount: Int = 0
}

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
fun Idiom.toEntity(): IdiomEntity = IdiomEntity().apply {
    val idiom = this@toEntity
    this.id = idiom.word // 단어 자체가 고유하므로 word를 ID로 사용
    this.word = idiom.word
    this.meaning = idiom.meaning
    this.hanja = idiom.hanja
    this.level = idiom.level
}
