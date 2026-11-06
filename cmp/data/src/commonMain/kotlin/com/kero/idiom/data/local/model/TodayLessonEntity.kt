package com.kero.idiom.data.local.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class TodayLessonEntity : RealmObject {
    @PrimaryKey
    var day: Long = 0
    var completed: Boolean = false
    var snapshot: String = ""
}

class TodayLessonReviewEntity : RealmObject {
    @PrimaryKey
    var word: String = ""
    var dueDay: Long = 0
    var lastStudiedDay: Long = 0
    var question: String = ""
}
