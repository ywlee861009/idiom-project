package com.kero.idiom.domain.usecase

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class RecordCorrectAnswerUseCaseTest {

    private val fakeRepo = FakeIdiomRepository()
    private val useCase = RecordCorrectAnswerUseCase(fakeRepo)

    @Test
    fun invoke_recordsCorrectAnswer() = runTest {
        useCase("일석이조")

        assertEquals(1, fakeRepo.correctAnswerWords.size)
        assertEquals("일석이조", fakeRepo.correctAnswerWords[0])
    }

    @Test
    fun invoke_multipleCalls_recordsAll() = runTest {
        useCase("일석이조")
        useCase("사면초가")
        useCase("오매불망")

        assertEquals(3, fakeRepo.correctAnswerWords.size)
        assertEquals("사면초가", fakeRepo.correctAnswerWords[1])
    }
}
