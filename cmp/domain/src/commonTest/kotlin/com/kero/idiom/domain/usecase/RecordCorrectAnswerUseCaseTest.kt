package com.kero.idiom.domain.usecase

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

/** 정답 기록 UseCase가 IdiomRepository에 정답 사자성어를 올바르게 저장하는지 검증 */
class RecordCorrectAnswerUseCaseTest {

    private val fakeRepo = FakeIdiomRepository()
    private val useCase = RecordCorrectAnswerUseCase(fakeRepo)

    /** 정답 사자성어 단어가 Repository에 정확히 기록되는지 검증 */
    @Test
    fun invoke_recordsCorrectAnswer() = runTest {
        useCase("일석이조")

        assertEquals(1, fakeRepo.correctAnswerWords.size)
        assertEquals("일석이조", fakeRepo.correctAnswerWords[0])
    }

    /** 여러 정답을 연속으로 기록할 때 모두 순서대로 저장되는지 검증 */
    @Test
    fun invoke_multipleCalls_recordsAll() = runTest {
        useCase("일석이조")
        useCase("사면초가")
        useCase("오매불망")

        assertEquals(3, fakeRepo.correctAnswerWords.size)
        assertEquals("사면초가", fakeRepo.correctAnswerWords[1])
    }
}
