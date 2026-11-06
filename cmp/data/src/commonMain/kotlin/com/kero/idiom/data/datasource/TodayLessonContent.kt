package com.kero.idiom.data.datasource

import com.kero.idiom.domain.model.Idiom
import com.kero.idiom.domain.model.TodayLesson
import com.kero.idiom.domain.model.TodayLessonQuestion
import kotlinx.serialization.Serializable

private const val QUESTIONS_PER_LESSON = 3
private const val FALLBACK_THEME = "뜻을 떠올리는 시간"

internal class TodayLessonContent(
    private val curatedLessons: List<CuratedTodayLesson>,
    idioms: List<Idiom>
) {
    private val idiomsByWord = idioms.associateBy { it.word }
    private val fallbackIdioms = idiomsByWord.values.filter { it.word.length == 4 }
        .sortedWith(compareBy<Idiom> { it.level }.thenBy { it.word })

    fun themeFor(day: Long, firstDay: Long): String =
        curatedLessonFor(day, firstDay)?.theme ?: FALLBACK_THEME

    fun create(
        day: Long,
        firstDay: Long,
        reviews: List<TodayLessonQuestion>,
        seenWords: Set<String>
    ): TodayLesson {
        val reviewWords = reviews.mapTo(mutableSetOf()) { it.idiom.word }
        val curated = curatedLessonFor(day, firstDay)
        val curatedQuestions = curated?.questions.orEmpty().asSequence().map { it.toQuestion() }
        val fallback = fallbackQuestions(day, firstDay)
        val candidates = curatedQuestions + fallback
        val newQuestions = (candidates.filter { it.idiom.word !in seenWords } + candidates)
            .filter { it.idiom.word !in reviewWords }
            .distinctBy { it.idiom.word }
            .take(QUESTIONS_PER_LESSON - reviews.size)
            .toList()

        check(reviews.size + newQuestions.size == QUESTIONS_PER_LESSON)
        return TodayLesson(
            day = day,
            theme = curated?.theme ?: FALLBACK_THEME,
            questions = reviews + newQuestions
        )
    }

    private fun curatedLessonFor(day: Long, firstDay: Long): CuratedTodayLesson? {
        val offset = day - firstDay
        return if (offset in 0L until curatedLessons.size.toLong()) {
            curatedLessons[offset.toInt()]
        } else {
            null
        }
    }

    private fun CuratedTodayLessonQuestion.toQuestion() = TodayLessonQuestion(
        idiom = idiomsByWord.getValue(word),
        prompt = prompt,
        alternatePrompt = alternatePrompt,
        options = options,
        hint = hint
    )

    private fun fallbackQuestions(day: Long, firstDay: Long): Sequence<TodayLessonQuestion> {
        val start = ((day - firstDay) * QUESTIONS_PER_LESSON).mod(fallbackIdioms.size)
        return fallbackIdioms.indices.asSequence().mapNotNull { offset ->
            val position = (start + offset) % fallbackIdioms.size
            val idiom = fallbackIdioms[position]
            val distractors = fallbackIdioms.asSequence()
                .drop(position + 1)
                .plus(fallbackIdioms.asSequence().take(position))
                .filter { it.meaning != idiom.meaning && it.hanja != idiom.hanja }
                .take(QUESTIONS_PER_LESSON - 1)
                .map { it.word }
                .toList()
            if (distractors.size != QUESTIONS_PER_LESSON - 1) return@mapNotNull null
            val answerPosition = (start + offset).mod(QUESTIONS_PER_LESSON)
            val options = distractors.toMutableList().apply { add(answerPosition, idiom.word) }
            val meaning = idiom.meaning.trimEnd('.', '。')
            TodayLessonQuestion(
                idiom = idiom,
                prompt = "‘${meaning}’이라는 뜻에 맞는 성어를 골라 보세요.",
                alternatePrompt = "‘${idiom.hanja}’를 한글로 어떻게 읽을까요?",
                options = options,
                hint = "한자로는 ‘${idiom.hanja}’라고 써요.",
                alternateHint = "‘${meaning}’이라는 뜻이에요."
            )
        }
    }
}

@Serializable
internal data class CuratedTodayLesson(
    val theme: String,
    val questions: List<CuratedTodayLessonQuestion>
)

@Serializable
internal data class CuratedTodayLessonQuestion(
    val word: String,
    val prompt: String,
    val alternatePrompt: String,
    val hint: String,
    val options: List<String>
)
