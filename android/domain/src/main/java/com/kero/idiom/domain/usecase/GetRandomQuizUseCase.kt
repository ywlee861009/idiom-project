package com.kero.idiom.domain.usecase

import com.kero.idiom.domain.model.Idiom
import com.kero.idiom.domain.model.Quiz
import com.kero.idiom.domain.repository.IdiomRepository
import javax.inject.Inject

class GetRandomQuizUseCase @Inject constructor(
    private val repository: IdiomRepository
) {
    suspend operator fun invoke(): Quiz {
        val allIdioms = repository.getIdioms()
        val idiom = allIdioms.random()
        
        // 4글자 중 랜덤하게 하나를 비움
        val blankIndex = (0 until 4).random()
        val answerChar = idiom.word[blankIndex]
        
        val questionWord = idiom.word.mapIndexed { index, c ->
            if (index == blankIndex) '_' else c
        }.joinToString("")

        // 오답 보기 생성 (전체 사자성어 글자 중 랜덤하게 3개 추출)
        val options = mutableSetOf<Char>()
        options.add(answerChar)
        
        while (options.size < 4) {
            val randomIdiom = allIdioms.random()
            val randomChar = randomIdiom.word.random()
            if (randomChar != answerChar) {
                options.add(randomChar)
            }
        }

        return Quiz(
            originalIdiom = idiom,
            questionWord = questionWord,
            answerChar = answerChar,
            options = options.toList().shuffled()
        )
    }
}
