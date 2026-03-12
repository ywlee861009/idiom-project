package com.kero.idiom.data.local

import com.kero.idiom.data.local.model.IdiomEntity
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

/**
 * Realm 데이터베이스 인스턴스를 제공하는 싱글톤 객체.
 * Room의 IdiomDatabase를 대체합니다.
 */
object RealmDatabase {
    /**
     * Realm 설정 정의.
     * [schema]에 사용할 RealmObject 클래스들을 등록합니다.
     */
    val config = RealmConfiguration.Builder(
        schema = setOf(IdiomEntity::class)
    )
    .name("idiom.realm") // DB 파일명
    .schemaVersion(1)    // 마이그레이션 버전
    .deleteRealmIfMigrationNeeded() // 💡 마이그레이션 실패 시 DB 초기화 (개발 편의성 최고!)
    .build()

    /**
     * Realm 인스턴스 생성.
     * Koin을 통해 싱글톤으로 주입받아 사용할 것을 권장합니다.
     */
    val realm: Realm by lazy { Realm.open(config) }
}
