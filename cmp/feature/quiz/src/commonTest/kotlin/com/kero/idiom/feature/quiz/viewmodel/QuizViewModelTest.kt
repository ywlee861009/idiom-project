package com.kero.idiom.feature.quiz.viewmodel

import app.cash.turbine.test
import com.kero.idiom.domain.model.Quiz
import com.kero.idiom.domain.model.UserStats
import com.kero.idiom.feature.quiz.contract.QuizIntent
import com.kero.idiom.feature.quiz.contract.QuizSideEffect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class QuizViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(deps: TestDependencies = createTestDependencies()): QuizViewModel {
        return QuizViewModel(
            getRandomQuizUseCase = deps.getRandomQuizUseCase,
            recordCorrectAnswerUseCase = deps.recordCorrectAnswerUseCase,
            updateUserStatsUseCase = deps.updateUserStatsUseCase,
            getUserStatsUseCase = deps.getUserStatsUseCase,
            adController = deps.adController
        )
    }

    /** 퀴즈 유형에 관계없이 정답을 제출하는 헬퍼 */
    private fun QuizViewModel.submitCorrectAnswer(quiz: Quiz) {
        if (quiz.options.isNotEmpty()) {
            handleIntent(QuizIntent.SelectOption(quiz.answer))
        } else {
            val blankChars = quiz.blankIndices.map { quiz.answer[it] }.joinToString("")
            handleIntent(QuizIntent.InputAnswer(blankChars))
            handleIntent(QuizIntent.SubmitAnswer)
        }
    }

    /** 퀴즈 유형에 관계없이 오답을 제출하는 헬퍼 */
    private fun QuizViewModel.submitWrongAnswer(quiz: Quiz) {
        if (quiz.options.isNotEmpty()) {
            val wrongOption = quiz.options.first { it != quiz.answer }
            handleIntent(QuizIntent.SelectOption(wrongOption))
        } else {
            handleIntent(QuizIntent.InputAnswer("틀린답틀린"))
            handleIntent(QuizIntent.SubmitAnswer)
        }
    }

    @Test
    fun init_loadsFirstQuiz() = runTest(testDispatcher) {
        val vm = createViewModel()
        advanceUntilIdle()

        val state = vm.state.value
        assertNotNull(state.currentQuiz)
        assertEquals(1, state.quizCount)
        assertFalse(state.isLoading)
    }

    @Test
    fun init_loadsGlobalCombo() = runTest(testDispatcher) {
        val deps = createTestDependencies()
        deps.userStatsRepo.stats.value = UserStats(globalCombo = 5)

        val vm = createViewModel(deps)
        advanceUntilIdle()

        assertEquals(5, vm.state.value.comboCount)
    }

    @Test
    fun init_loadsAds() = runTest(testDispatcher) {
        val deps = createTestDependencies()
        val vm = createViewModel(deps)
        advanceUntilIdle()

        assertTrue(deps.adController.interstitialLoaded)
        assertTrue(deps.adController.rewardedAdLoaded)
    }

    @Test
    fun correctAnswer_updatesState() = runTest(testDispatcher) {
        val vm = createViewModel()
        advanceUntilIdle()

        // sideEffect 채널을 드레인하여 send()가 suspend되지 않도록 함
        val collector = launch { vm.sideEffect.collect {} }

        val quiz = vm.state.value.currentQuiz!!
        vm.submitCorrectAnswer(quiz)
        advanceUntilIdle()

        assertTrue(vm.state.value.isCorrect == true)
        assertEquals(1, vm.state.value.score)
        collector.cancel()
    }

    @Test
    fun wrongAnswer_resetsCombo() = runTest(testDispatcher) {
        val vm = createViewModel()
        advanceUntilIdle()

        val collector = launch { vm.sideEffect.collect {} }

        val quiz = vm.state.value.currentQuiz!!
        vm.submitWrongAnswer(quiz)
        advanceUntilIdle()

        assertEquals(false, vm.state.value.isCorrect)
        assertEquals(0, vm.state.value.comboCount)
        collector.cancel()
    }

    @Test
    fun alreadyAnswered_isIgnored() = runTest(testDispatcher) {
        val vm = createViewModel()
        advanceUntilIdle()

        val collector = launch { vm.sideEffect.collect {} }

        val quiz = vm.state.value.currentQuiz!!
        vm.submitCorrectAnswer(quiz)
        advanceUntilIdle()
        val scoreAfterFirst = vm.state.value.score

        vm.submitCorrectAnswer(quiz)
        advanceUntilIdle()

        assertEquals(scoreAfterFirst, vm.state.value.score)
        collector.cancel()
    }

    @Test
    fun inputAnswer_updatesInputText() = runTest(testDispatcher) {
        val vm = createViewModel()
        advanceUntilIdle()

        val input = "\ud14c\uc2a4\ud2b8"
        vm.handleIntent(QuizIntent.InputAnswer(input))

        assertEquals(input, vm.state.value.inputText)
    }

    @Test
    fun nextQuiz_loadsNewQuiz() = runTest(testDispatcher) {
        val vm = createViewModel()
        advanceUntilIdle()

        vm.handleIntent(QuizIntent.NextQuiz)
        advanceUntilIdle()

        assertEquals(2, vm.state.value.quizCount)
        assertNull(vm.state.value.selectedOption)
        assertNull(vm.state.value.isCorrect)
        assertEquals("", vm.state.value.inputText)
    }

    @Test
    fun nextQuiz_afterAllQuizzes_navigatesToResult() = runTest(testDispatcher) {
        val vm = createViewModel()
        advanceUntilIdle()

        repeat(4) {
            vm.handleIntent(QuizIntent.NextQuiz)
            advanceUntilIdle()
        }
        assertEquals(5, vm.state.value.quizCount)

        vm.sideEffect.test {
            vm.handleIntent(QuizIntent.NextQuiz)
            advanceUntilIdle()

            val effect = awaitItem()
            assertTrue(effect is QuizSideEffect.NavigateToResult)
        }
    }

    @Test
    fun showHint_whenAdAvailable_revealsHint() = runTest(testDispatcher) {
        val deps = createTestDependencies()
        deps.adController.rewardedAdAvailable = true
        val vm = createViewModel(deps)
        advanceUntilIdle()

        vm.handleIntent(QuizIntent.ShowHint)

        assertTrue(vm.state.value.isHintRevealed)
    }

    @Test
    fun showHint_whenAdUnavailable_showsToast() = runTest(testDispatcher) {
        val deps = createTestDependencies()
        deps.adController.rewardedAdAvailable = false
        val vm = createViewModel(deps)
        advanceUntilIdle()

        vm.sideEffect.test {
            vm.handleIntent(QuizIntent.ShowHint)
            advanceUntilIdle()

            val effect = awaitItem()
            assertTrue(effect is QuizSideEffect.ShowToast)
        }
    }

    @Test
    fun correctAnswer_incrementsComboAndXp() = runTest(testDispatcher) {
        val vm = createViewModel()
        advanceUntilIdle()

        val collector = launch { vm.sideEffect.collect {} }

        val quiz = vm.state.value.currentQuiz!!
        vm.submitCorrectAnswer(quiz)
        advanceUntilIdle()

        assertEquals(1, vm.state.value.comboCount)
        assertTrue(vm.state.value.totalXpGained > 0)
        collector.cancel()
    }

    @Test
    fun comboXpBonus_correctCalculation() = runTest(testDispatcher) {
        val deps = createTestDependencies()
        deps.userStatsRepo.stats.value = UserStats(globalCombo = 1)
        val vm = createViewModel(deps)
        advanceUntilIdle()

        val collector = launch { vm.sideEffect.collect {} }

        val quiz = vm.state.value.currentQuiz!!
        val baseXp = quiz.originalIdiom.level
        vm.submitCorrectAnswer(quiz)
        advanceUntilIdle()

        // combo 1→2 → bonus = (2-1).coerceIn(0,5) = 1
        assertEquals(baseXp + 1, vm.state.value.totalXpGained)
        collector.cancel()
    }
}
