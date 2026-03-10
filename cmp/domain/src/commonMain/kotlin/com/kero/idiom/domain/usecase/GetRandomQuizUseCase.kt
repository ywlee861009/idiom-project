package com.kero.idiom.domain.usecase

import com.kero.idiom.domain.model.Idiom
import com.kero.idiom.domain.model.Quiz
import com.kero.idiom.domain.model.QuizType
import com.kero.idiom.domain.repository.IdiomRepository
class GetRandomQuizUseCase(
    private val repository: IdiomRepository
) {
    suspend operator fun invoke(): Quiz {
        // [Smart Selection] 노출 빈도가 낮은 사자성어 1개를 우선적으로 가져옵니다.
        val targetIdioms = repository.getRandomIdioms(limit = 1)
        val idiom = targetIdioms.first()

        // 오답 보기를 만들기 위한 샘플 데이터 (이것도 노출 빈도가 낮은 것들 위주로 섞음)
        val dummyIdioms = repository.getRandomIdioms(limit = 20)

        // 출제 기록 (ID-21 핵심)
        repository.recordExposure(idiom.word)

        val type = QuizType.entries.random()

        return when (type) {
            QuizType.FILL_BLANK -> createFillBlankQuiz(idiom, dummyIdioms)
            QuizType.MEANING_TO_WORD -> createMeaningToWordQuiz(idiom, dummyIdioms)
            QuizType.HANJA_TO_HANGUL -> createHanjaToHangulQuiz(idiom, dummyIdioms)
        }
    }

    private fun createFillBlankQuiz(idiom: Idiom, samples: List<Idiom>): Quiz {
        val blankIndex = (0 until 4).random()
        val answerChar = idiom.word[blankIndex].toString()
        val questionText = idiom.word.mapIndexed { index, c ->
            if (index == blankIndex) '_' else c
        }.joinToString("")

        val options = mutableSetOf<String>()
        options.add(answerChar)
        while (options.size < 4) {
            val randomChar = samples.random().word.random().toString()
            options.add(randomChar)
        }
// ...

        return Quiz(
            type = QuizType.FILL_BLANK,
            originalIdiom = idiom,
            questionText = questionText,
            hintText = idiom.meaning,
            answer = answerChar,
            options = options.toList().shuffled()
        )
    }

    private fun createMeaningToWordQuiz(idiom: Idiom, samples: List<Idiom>): Quiz {
        val options = mutableSetOf<String>()
        options.add(idiom.word)
        while (options.size < 4) {
            options.add(samples.random().word)
        }

        return Quiz(
            type = QuizType.MEANING_TO_WORD,
            originalIdiom = idiom,
            questionText = idiom.meaning, // 뜻을 크게 보여줌
            hintText = idiom.hanja,       // 한자를 힌트로
            answer = idiom.word,
            options = options.toList().shuffled()
        )
    }

    private fun createHanjaToHangulQuiz(idiom: Idiom, samples: List<Idiom>): Quiz {
        val options = mutableSetOf<String>()
        options.add(idiom.word)
        while (options.size < 4) {
            options.add(samples.random().word)
        }

        return Quiz(
            type = QuizType.HANJA_TO_HANGUL,
            originalIdiom = idiom,
            questionText = idiom.hanja, // 한자를 크게 보여줌
            hintText = idiom.meaning,   // 뜻을 힌트로
            answer = idiom.word,
            options = options.toList().shuffled()
        )
    }
}
