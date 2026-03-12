package com.kero.idiom.domain.usecase

import com.kero.idiom.domain.model.Idiom
import com.kero.idiom.domain.model.Quiz
import com.kero.idiom.domain.model.QuizType
import com.kero.idiom.domain.repository.IdiomRepository
/**
 * 지능적 알고리즘을 통해 사자성어 퀴즈를 생성하는 UseCase입니다.
 * [Smart Selection]을 통해 노출 빈도가 낮은 성어를 우선적으로 출제하며, 
 * 다양한 문제 유형(객관식, 주관식, 빈칸 채우기 등)을 동적으로 생성합니다.
 *
 * @property repository 사자성어 데이터에 접근하기 위한 저장소 인터페이스
 */
class GetRandomQuizUseCase(
    private val repository: IdiomRepository
) {
    /**
     * 새로운 퀴즈 세션을 위한 한 문제를 생성하여 반환합니다.
     * 1. DB에서 노출 빈도([exposureCount])가 낮은 성어를 1개 선택합니다.
     * 2. 선택된 성어의 노출 기록을 업데이트합니다.
     * 3. 5가지 문제 유형 중 하나를 랜덤하게 결정하여 퀴즈 객체를 생성합니다.
     *
     * @return 생성된 [Quiz] 객체
     * @throws IllegalStateException DB가 비어있거나 데이터를 불러올 수 없는 경우 발생
     */
    suspend operator fun invoke(): Quiz {
        // [Smart Selection] 노출 빈도가 낮은 사자성어 1개를 우선적으로 가져옵니다.
        val targetIdioms = repository.getRandomIdioms(limit = 1)
        val idiom = targetIdioms.firstOrNull()
            ?: throw IllegalStateException("사자성어 데이터가 없습니다. DB 초기화를 확인하세요.")

        // 오답 보기를 만들기 위한 샘플 데이터 (이것도 노출 빈도가 낮은 것들 위주로 섞음)
        val dummyIdioms = repository.getRandomIdioms(limit = 20)

        // 출제 기록 (ID-21 핵심)
        repository.recordExposure(idiom.word)

        val type = QuizType.entries.random()

        return when (type) {
            QuizType.FILL_BLANK -> createFillBlankQuiz(idiom, dummyIdioms)
            QuizType.MEANING_TO_WORD -> createMeaningToWordQuiz(idiom, dummyIdioms)
            QuizType.HANJA_TO_HANGUL -> createHanjaToHangulQuiz(idiom, dummyIdioms)
            QuizType.FILL_BLANKS_2 -> createFillMultipleBlanksQuiz(idiom, 2)
            QuizType.FILL_BLANKS_4 -> createFillMultipleBlanksQuiz(idiom, 4)
        }
    }

    /**
     * 주관식 빈칸 채우기(2칸 또는 4칸) 퀴즈를 생성합니다.
     *
     * @param idiom 출제할 사자성어
     * @param blankCount 비워둘 글자 수 (2개 또는 4개)
     * @return 생성된 [Quiz] 객체
     */
    private fun createFillMultipleBlanksQuiz(idiom: Idiom, blankCount: Int): Quiz {
        val blankIndices = (0 until 4).shuffled().take(blankCount).sorted()
        val questionText = idiom.word.mapIndexed { index, c ->
            if (index in blankIndices) '_' else c
        }.joinToString("")

        return Quiz(
            type = if (blankCount == 2) QuizType.FILL_BLANKS_2 else QuizType.FILL_BLANKS_4,
            originalIdiom = idiom,
            questionText = questionText,
            hintText = idiom.meaning,
            answer = idiom.word,
            options = emptyList(),
            blankIndices = blankIndices
        )
    }

    /**
     * 4개 글자 중 1개만 비워둔 객관식 빈칸 채우기 퀴즈를 생성합니다.
     *
     * @param idiom 출제할 사자성어
     * @param samples 오답 보기를 생성하기 위한 사자성어 리스트
     * @return 생성된 [Quiz] 객체
     */
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

        return Quiz(
            type = QuizType.FILL_BLANK,
            originalIdiom = idiom,
            questionText = questionText,
            hintText = idiom.meaning,
            answer = answerChar,
            options = options.toList().shuffled(),
            blankIndices = listOf(blankIndex)
        )
    }

    /**
     * 사자성어의 의미(뜻)를 보고 정답 단어를 맞히는 객관식 퀴즈를 생성합니다.
     *
     * @param idiom 출제할 사자성어
     * @param samples 오답 보기를 생성하기 위한 사자성어 리스트
     * @return 생성된 [Quiz] 객체
     */
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

    /**
     * 사자성어의 한자를 보고 정답 단어를 맞히는 객관식 퀴즈를 생성합니다.
     *
     * @param idiom 출제할 사자성어
     * @param samples 오답 보기를 생성하기 위한 사자성어 리스트
     * @return 생성된 [Quiz] 객체
     */
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

