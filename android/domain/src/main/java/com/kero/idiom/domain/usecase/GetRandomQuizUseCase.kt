package com.kero.idiom.domain.usecase

import com.kero.idiom.domain.model.Idiom
import com.kero.idiom.domain.model.Quiz
import com.kero.idiom.domain.model.QuizType
import com.kero.idiom.domain.repository.IdiomRepository
import javax.inject.Inject

class GetRandomQuizUseCase @Inject constructor(
    private val repository: IdiomRepository
) {
    suspend operator fun invoke(): Quiz {
        val allIdioms = repository.getIdioms()
        val idiom = allIdioms.random()
        val type = QuizType.entries.random()

        return when (type) {
            QuizType.FILL_BLANK -> createFillBlankQuiz(idiom, allIdioms)
            QuizType.MEANING_TO_WORD -> createMeaningToWordQuiz(idiom, allIdioms)
            QuizType.HANJA_TO_HANGUL -> createHanjaToHangulQuiz(idiom, allIdioms)
        }
    }

    private fun createFillBlankQuiz(idiom: Idiom, allIdioms: List<Idiom>): Quiz {
        val blankIndex = (0 until 4).random()
        val answerChar = idiom.word[blankIndex].toString()
        val questionText = idiom.word.mapIndexed { index, c ->
            if (index == blankIndex) '_' else c
        }.joinToString("")

        val options = mutableSetOf<String>()
        options.add(answerChar)
        while (options.size < 4) {
            val randomChar = allIdioms.random().word.random().toString()
            options.add(randomChar)
        }

        return Quiz(
            type = QuizType.FILL_BLANK,
            originalIdiom = idiom,
            questionText = questionText,
            hintText = idiom.meaning,
            answer = answerChar,
            options = options.toList().shuffled()
        )
    }

    private fun createMeaningToWordQuiz(idiom: Idiom, allIdioms: List<Idiom>): Quiz {
        val options = mutableSetOf<String>()
        options.add(idiom.word)
        while (options.size < 4) {
            options.add(allIdioms.random().word)
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

    private fun createHanjaToHangulQuiz(idiom: Idiom, allIdioms: List<Idiom>): Quiz {
        val options = mutableSetOf<String>()
        options.add(idiom.word)
        while (options.size < 4) {
            options.add(allIdioms.random().word)
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
