package com.kero.idiom.data.repository

import com.kero.idiom.data.datasource.CuratedTodayLesson
import com.kero.idiom.data.datasource.CuratedTodayLessonQuestion
import com.kero.idiom.data.datasource.TodayLessonContent
import com.kero.idiom.data.local.model.IdiomEntity
import com.kero.idiom.data.local.model.TodayLessonEntity
import com.kero.idiom.data.local.model.TodayLessonReviewEntity
import com.kero.idiom.data.local.model.toEntity
import com.kero.idiom.domain.model.Idiom
import com.kero.idiom.domain.model.TodayLessonQuestion
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import kotlinx.coroutines.async
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class TodayLessonRepositoryImplTest {
    private lateinit var realm: Realm
    private val idioms = listOf("고진감래", "전화위복", "우공이산", "유비무환", "일석이조", "과유불급").map {
        Idiom(word = it, hanja = "한자 $it", meaning = "뜻 $it", level = 1)
    }
    private val curated = CuratedTodayLesson(
        theme = "끝까지 해낸 순간",
        questions = idioms.take(3).map { idiom ->
            CuratedTodayLessonQuestion(
                word = idiom.word,
                prompt = "첫 장면 ${idiom.word}",
                alternatePrompt = "다른 장면 ${idiom.word}",
                options = idioms.take(3).map { it.word },
                hint = "단서"
            )
        }
    )

    @BeforeTest
    fun setUp() {
        val config = RealmConfiguration.Builder(
            schema = setOf(IdiomEntity::class, TodayLessonEntity::class, TodayLessonReviewEntity::class)
        ).name("today-lesson-test.realm").inMemory().build()
        realm = Realm.open(config)
    }

    @AfterTest
    fun tearDown() {
        realm.close()
    }

    @Test
    fun previewDoesNotCreateSessionAndNewRepositoryResumesSavedHintAndAnswer() = runTest {
        val repo = repository()
        assertEquals(curated.theme, repo.previewTheme(MONDAY))
        assertNull(repo.get(MONDAY))
        assertFalse(repo.isCompleted(MONDAY))

        val original = repo.start(MONDAY)
        assertEquals(original, repo.start(MONDAY))
        repo.revealHint(MONDAY, 0)
        val resumed = repository()
        val afterHint = assertNotNull(resumed.get(MONDAY))
        assertTrue(afterHint.questions.first().isHintRevealed)
        assertEquals(original.questions.first().options, afterHint.questions.first().options)
        assertEquals(0, resumed.advance(MONDAY, 0).currentIndex)

        val choice = original.questions.first().idiom.word
        resumed.submitAnswer(MONDAY, 0, choice)
        assertEquals(choice, repository().start(MONDAY).questions.first().selectedAnswer)
        assertEquals(1, resumed.advance(MONDAY, 0).currentIndex)
        assertEquals(1, resumed.advance(MONDAY, 0).currentIndex)
        assertNull(resumed.submitAnswer(MONDAY, 0, choice).questions[1].selectedAnswer)
    }

    @Test
    fun finalAnswerCommitsSingleStampAndOnlyActuallyNewAcquisitionsEvenOnConcurrentRetry() = runTest {
        realm.write { copyToRealm(idioms.first().toEntity().apply { correctCount = 4 }) }
        val repo = repository()
        val lesson = repo.start(MONDAY)
        for (index in 0..1) {
            repo.submitAnswer(MONDAY, index, lesson.questions[index].idiom.word)
            repo.advance(MONDAY, index)
        }
        val choice = lesson.questions.last().idiom.word
        val first = async { repo.submitAnswer(MONDAY, 2, choice) }
        val retry = async { repo.submitAnswer(MONDAY, 2, choice) }
        val result = first.await()
        assertEquals(result, retry.await())
        assertTrue(result.isCompleted)
        assertFalse(result.isResultVisible)
        assertEquals(3, result.correctCount)
        assertEquals(idioms.slice(1..2), result.newlyCollected)
        assertEquals(5, realm.query<IdiomEntity>("word == $0", idioms.first().word).first().find()?.correctCount)
        assertEquals(1, realm.query<IdiomEntity>("word == $0", choice).first().find()?.correctCount)
        assertEquals(1, repo.countCompletedDaysThisWeek(MONDAY + 6))
        assertTrue(repo.isFirstCompletion(MONDAY))
        assertTrue(repository().advance(MONDAY, 2).isResultVisible)
        assertEquals(result.newlyCollected, repository().start(MONDAY).newlyCollected)
    }

    @Test
    fun allWrongStillCompletesAndOverdueReviewsUseStableWordOrderWithOneFreshQuestion() = runTest {
        val repo = repository()
        val lesson = repo.start(MONDAY)
        lesson.questions.forEachIndexed { index, question ->
            repo.submitAnswer(MONDAY, index, wrongChoice(question))
            repo.advance(MONDAY, index)
        }
        val result = repo.start(MONDAY)
        assertEquals(0, result.correctCount)
        assertEquals(emptyList(), result.newlyCollected)
        assertEquals(1, repo.countCompletedDaysThisWeek(MONDAY))

        val next = repo.start(MONDAY + 1)
        val reviews = next.questions.filter { it.isReview }
        val expectedWords = lesson.questions.map { it.idiom.word }.sorted().take(2)
        assertEquals(expectedWords, reviews.map { it.idiom.word })
        assertEquals(3, next.questions.map { it.idiom.word }.distinct().size)
        assertTrue(next.questions.last().idiom.word !in lesson.questions.map { it.idiom.word })
        reviews.forEach { review ->
            val old = lesson.questions.single { it.idiom.word == review.idiom.word }
            assertEquals(old.alternatePrompt, review.prompt)
            assertNotEquals(old.options, review.options)
        }
        val remaining = lesson.questions.single { it.idiom.word !in expectedWords }
        assertEquals(MONDAY + 1, reviewDue(remaining.idiom.word))
    }

    @Test
    fun midnightCompletionUsesActualDayForReviewAndStartDayForStamp() = runTest {
        val repo = repository()
        val sunday = MONDAY - 1
        val lesson = repo.start(sunday)
        repo.submitAnswer(sunday, 0, wrongChoice(lesson.questions[0]), completionDay = MONDAY)
        repo.advance(sunday, 0)
        repo.revealHint(sunday, 1)
        repo.submitAnswer(sunday, 1, lesson.questions[1].idiom.word, completionDay = MONDAY)
        repo.advance(sunday, 1)
        repo.submitAnswer(sunday, 2, lesson.questions[2].idiom.word, completionDay = MONDAY)

        assertTrue(repo.isCompleted(sunday))
        assertFalse(repo.isCompleted(MONDAY))
        assertEquals(1, repo.countCompletedDaysThisWeek(sunday))
        assertEquals(0, repo.countCompletedDaysThisWeek(MONDAY))
        assertEquals(listOf(MONDAY + 1, MONDAY + 1, MONDAY + 3), lesson.questions.map { reviewDue(it.idiom.word) })
        assertTrue(repo.start(MONDAY).questions.none { it.isReview })

        val tuesday = MONDAY + 1
        val next = repo.start(tuesday)
        val reviews = next.questions.filter { it.isReview }
        assertEquals(2, reviews.size)
        next.questions.forEachIndexed { index, question ->
            repo.submitAnswer(tuesday, index, question.idiom.word)
            repo.advance(tuesday, index)
        }
        reviews.forEach { assertEquals(tuesday + 7, reviewDue(it.idiom.word)) }
        assertEquals(1, repo.countCompletedDaysThisWeek(tuesday))
        assertFalse(repo.isFirstCompletion(sunday))
    }

    private fun repository() = TodayLessonRepositoryImpl(realm) {
        TodayLessonContent(listOf(curated), idioms)
    }

    private fun reviewDue(word: String): Long? =
        realm.query<TodayLessonReviewEntity>("word == $0", word).first().find()?.dueDay

    private fun wrongChoice(question: TodayLessonQuestion): String =
        question.options.first { it != question.idiom.word }

    private companion object {
        const val MONDAY = 20_003L
    }
}
