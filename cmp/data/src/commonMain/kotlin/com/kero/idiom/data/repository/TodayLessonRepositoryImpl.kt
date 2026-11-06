package com.kero.idiom.data.repository

import com.kero.idiom.data.datasource.TodayLessonContent
import com.kero.idiom.data.local.model.IdiomEntity
import com.kero.idiom.data.local.model.TodayLessonEntity
import com.kero.idiom.data.local.model.TodayLessonReviewEntity
import com.kero.idiom.data.local.model.toDomain
import com.kero.idiom.data.local.model.toEntity
import com.kero.idiom.domain.model.TodayLesson
import com.kero.idiom.domain.model.TodayLessonQuestion
import com.kero.idiom.domain.repository.TodayLessonRepository
import io.realm.kotlin.MutableRealm
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class TodayLessonRepositoryImpl(
    private val realm: Realm,
    private val loadContent: suspend () -> TodayLessonContent
) : TodayLessonRepository {
    private val contentMutex = Mutex()
    private var cachedContent: TodayLessonContent? = null

    override suspend fun get(day: Long): TodayLesson? =
        realm.query<TodayLessonEntity>("day == $0", day).first().find()?.readLesson()

    override suspend fun previewTheme(day: Long): String {
        get(day)?.let { return it.theme }
        val firstDay = realm.query<TodayLessonEntity>().sort("day", Sort.ASCENDING)
            .first().find()?.day ?: day
        return content().themeFor(day, firstDay)
    }

    override suspend fun start(day: Long): TodayLesson {
        get(day)?.let { return it }
        val content = content()
        return realm.write {
            findLesson(day)?.readLesson() ?: run {
                val firstDay = query<TodayLessonEntity>().sort("day", Sort.ASCENDING)
                    .first().find()?.day ?: day
                val reviews = query<TodayLessonReviewEntity>("dueDay <= $0", day)
                    .sort("dueDay" to Sort.ASCENDING, "word" to Sort.ASCENDING)
                    .limit(2)
                    .find()
                    .map { Json.decodeFromString<TodayLessonQuestion>(it.question) }
                val seenWords = query<TodayLessonReviewEntity>().find().mapTo(mutableSetOf()) { it.word }
                val lesson = content.create(day, firstDay, reviews, seenWords)
                copyToRealm(TodayLessonEntity().apply {
                    this.day = day
                    snapshot = Json.encodeToString(lesson)
                })
                lesson
            }
        }
    }

    override suspend fun revealHint(day: Long, questionIndex: Int): TodayLesson =
        update(day, questionIndex) { lesson, question ->
            if (question.selectedAnswer != null) lesson
            else lesson.withQuestion(questionIndex, question.copy(isHintRevealed = true))
        }

    override suspend fun submitAnswer(
        day: Long,
        questionIndex: Int,
        choice: String,
        completionDay: Long
    ): TodayLesson =
        update(day, questionIndex) { lesson, question ->
            if (question.selectedAnswer != null || choice !in question.options) {
                lesson
            } else {
                val answered = lesson.withQuestion(questionIndex, question.copy(selectedAnswer = choice))
                if (answered.isCompleted) complete(answered, completionDay) else answered
            }
        }

    override suspend fun advance(day: Long, questionIndex: Int): TodayLesson =
        update(day, questionIndex) { lesson, question ->
            if (question.selectedAnswer == null) lesson
            else lesson.copy(currentIndex = lesson.currentIndex + 1)
        }

    override suspend fun countCompletedDaysThisWeek(day: Long): Int {
        val monday = day - (day + 3).mod(7)
        return realm.query<TodayLessonEntity>(
            "completed == true AND day >= $0 AND day < $1", monday, monday + 7
        ).count().find().toInt()
    }

    override suspend fun isCompleted(day: Long): Boolean =
        realm.query<TodayLessonEntity>("day == $0 AND completed == true", day).first().find() != null

    override suspend fun isFirstCompletion(day: Long): Boolean =
        isCompleted(day) && realm.query<TodayLessonEntity>("completed == true").count().find() == 1L

    private suspend fun content(): TodayLessonContent = contentMutex.withLock {
        cachedContent ?: loadContent().also { cachedContent = it }
    }

    private suspend fun update(
        day: Long,
        questionIndex: Int,
        change: MutableRealm.(TodayLesson, TodayLessonQuestion) -> TodayLesson
    ): TodayLesson = realm.write {
        val entity = checkNotNull(findLesson(day)) { "Today's lesson was never started." }
        val lesson = entity.readLesson()
        if (questionIndex != lesson.currentIndex || lesson.isResultVisible) return@write lesson

        val updated = change(lesson, lesson.questions[questionIndex])
        if (updated != lesson) {
            entity.snapshot = Json.encodeToString(updated)
            entity.completed = updated.isCompleted
        }
        updated
    }

    private fun MutableRealm.findLesson(day: Long): TodayLessonEntity? =
        query<TodayLessonEntity>("day == $0", day).first().find()

    private fun TodayLessonEntity.readLesson(): TodayLesson = Json.decodeFromString(snapshot)

    private fun TodayLesson.withQuestion(index: Int, question: TodayLessonQuestion): TodayLesson =
        copy(questions = questions.toMutableList().apply { set(index, question) })

    private fun MutableRealm.complete(lesson: TodayLesson, completionDay: Long): TodayLesson {
        val newIdioms = lesson.questions.mapNotNull { question ->
            updateReview(completionDay, question)
            if (question.isCorrect != true) return@mapNotNull null

            val idiom = query<IdiomEntity>("id == $0", question.idiom.word).first().find()
                ?: copyToRealm(question.idiom.toEntity().apply { isArchived = true })
            idiom.correctCount += 1
            if (idiom.correctCount == 1) idiom.toDomain() else null
        }
        return lesson.copy(newlyCollected = newIdioms)
    }

    private fun MutableRealm.updateReview(day: Long, question: TodayLessonQuestion) {
        val review = query<TodayLessonReviewEntity>("word == $0", question.idiom.word).first().find()
            ?: copyToRealm(TodayLessonReviewEntity().apply {
                word = question.idiom.word
                lastStudiedDay = day - 1
            })
        if (review.lastStudiedDay > day) return

        val unaided = question.isCorrect == true && !question.isHintRevealed
        val delayDays = when {
            !unaided -> 1
            question.isReview -> 7
            else -> 3
        }
        review.dueDay = day + delayDays
        review.lastStudiedDay = day
        review.question = Json.encodeToString(question.copy(
            prompt = question.alternatePrompt,
            alternatePrompt = question.prompt,
            options = question.options.drop(1) + question.options.first(),
            hint = question.alternateHint,
            alternateHint = question.hint,
            isReview = true,
            isHintRevealed = false,
            selectedAnswer = null
        ))
    }
}
