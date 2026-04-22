package com.kero.idiom.data.local.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class DailyRecordEntity : RealmObject {
    @PrimaryKey
    var date: String = ""          // "2026-04-22" 형식
    var solvedCount: Int = 0
    var correctCount: Int = 0
    var earnedXp: Int = 0
}
