package com.kero.idiom.feature.quiz.viewmodel

import app.cash.turbine.test
import com.kero.idiom.domain.model.Quiz
import com.kero.idiom.domain.model.QuizType
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

/** 퀴즈 진행(QuizViewModel)의 초기화, 정답/오답 처리, 콤보, 힌트, 화면 전환 로직 검증 */
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

    /** 퀴즈 유형(객관식/주관식/순서맞히기)에 관계없이 정답을 제출하는 헬퍼 */
    private fun QuizViewModel.submitCorrectAnswer(quiz: Quiz) {
        if (quiz.type == QuizType.ORDER_MATCH) {
            quiz.answer.forEach { answerChar ->
                val actualIndex = quiz.charPool.indices.first { idx ->
                    quiz.charPool[idx] == answerChar.toString() && idx !in state.value.usedPoolIndices
                }
                handleIntent(QuizIntent.SelectPoolLetter(actualIndex))
            }
        } else if (quiz.options.isNotEmpty()) {
            handleIntent(QuizIntent.SelectOption(quiz.answer))
        } else {
            val blankChars = quiz.blankIndices.map { quiz.answer[it] }.joinToString("")
            handleIntent(QuizIntent.InputAnswer(blankChars))
            handleIntent(QuizIntent.SubmitAnswer)
        }
    }

    /** 퀴즈 유형(객관식/주관식/순서맞히기)에 관계없이 오답을 제출하는 헬퍼 */
    private fun QuizViewModel.submitWrongAnswer(quiz: Quiz) {
        if (quiz.type == QuizType.ORDER_MATCH) {
            // 순서 맞히기: 오답 글자 4개를 순서대로 선택 (정답이 아닌 글자들)
            val wrongIndices = quiz.charPool.indices.filter { quiz.charPool[it] !in quiz.answer.map { c -> c.toString() } }
            wrongIndices.take(quiz.answer.length).forEach { idx ->
                handleIntent(QuizIntent.SelectPoolLetter(idx))
            }
        } else if (quiz.options.isNotEmpty()) {
            val wrongOption = quiz.options.first { it != quiz.answer }
            handleIntent(QuizIntent.SelectOption(wrongOption))
        } else {
            handleIntent(QuizIntent.InputAnswer("틀린답틀린"))
            handleIntent(QuizIntent.SubmitAnswer)
        }
    }

    /** ViewModel 생성 시 첫 번째 퀴즈가 자동 로드되고, 로딩이 완료되는지 검증 */
    @Test
    fun init_loadsFirstQuiz() = runTest(testDispatcher) {
        val vm = createViewModel()
        advanceUntilIdle()

        val state = vm.state.value
        assertNotNull(state.currentQuiz)
        assertEquals(1, state.quizCount)
        assertFalse(state.isLoading)
    }

    /** ViewModel 생성 시 DataStore에 저장된 글로벌 콤보가 세션에 복원되는지 검증 */
    @Test
    fun init_loadsGlobalCombo() = runTest(testDispatcher) {
        val deps = createTestDependencies()
        deps.userStatsRepo.stats.value = UserStats(globalCombo = 5)

        val vm = createViewModel(deps)
        advanceUntilIdle()

        assertEquals(5, vm.state.value.comboCount)
    }

    /** ViewModel 생성 시 전면 광고와 리워드 광고가 미리 로드(preload)되는지 검증 */
    @Test
    fun init_loadsAds() = runTest(testDispatcher) {
        val deps = createTestDependencies()
        val vm = createViewModel(deps)
        advanceUntilIdle()

        assertTrue(deps.adController.interstitialLoaded)
        assertTrue(deps.adController.rewardedAdLoaded)
    }

    /** 정답 제출 시 isCorrect=true로 변경되고 점수가 1 증가하는지 검증 */
    @Test
    fun correctAnswer_updatesState() = runTest(testDispatcher) {
        val vm = createViewModel()
        advanceUntilIdle()

        val collector = launch { vm.sideEffect.collect {} }

        val quiz = vm.state.value.currentQuiz!!
        vm.submitCorrectAnswer(quiz)
        advanceUntilIdle()

        assertTrue(vm.state.value.isCorrect == true)
        assertEquals(1, vm.state.value.score)
        collector.cancel()
    }

    /** 오답 제출 시 isCorrect=false로 변경되고 콤보가 0으로 초기화되는지 검증 */
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

    /** 이미 답변한 퀴즈에 중복 제출해도 점수가 변하지 않는지 검증 (이중 클릭 방어) */
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

    /** 주관식 입력 시 inputText 상태가 실시간으로 업데이트되는지 검증 */
    @Test
    fun inputAnswer_updatesInputText() = runTest(testDispatcher) {
        val vm = createViewModel()
        advanceUntilIdle()

        val input = "\ud14c\uc2a4\ud2b8"
        vm.handleIntent(QuizIntent.InputAnswer(input))

        assertEquals(input, vm.state.value.inputText)
    }

    /** NextQuiz 시 새 퀴즈가 로드되고, 이전 답변 상태가 초기화되는지 검증 */
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

    /** 5문제 완료 후 NextQuiz 시 결과 화면으로 네비게이션되는지 검증 */
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

    /** 리워드 광고가 준비되어 있을 때 힌트가 공개되는지 검증 */
    @Test
    fun showHint_whenAdAvailable_revealsHint() = runTest(testDispatcher) {
        val deps = createTestDependencies()
        deps.adController.rewardedAdAvailable = true
        val vm = createViewModel(deps)
        advanceUntilIdle()

        vm.handleIntent(QuizIntent.ShowHint)

        assertTrue(vm.state.value.isHintRevealed)
    }

    /** 리워드 광고가 준비되지 않았을 때 토스트 메시지가 표시되는지 검증 */
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

    /** 정답 시 콤보 카운트가 증가하고 XP가 획득되는지 검증 */
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

    /** ORDER_MATCH 퀴즈가 나올 때까지 새 VM을 생성하는 헬퍼 */
    private suspend fun createViewModelWithOrderMatch(dispatcher: kotlinx.coroutines.test.TestCoroutineScheduler): QuizViewModel {
        repeat(100) {
            val vm = createViewModel()
            dispatcher.advanceUntilIdle()
            if (vm.state.value.currentQuiz?.type == QuizType.ORDER_MATCH) return vm
        }
        throw IllegalStateException("ORDER_MATCH 퀴즈가 생성되지 않음")
    }

    /** ORDER_MATCH: 글자를 선택하면 selectedLetters에 추가되는지 검증 */
    @Test
    fun orderMatch_selectPoolLetter_addsToSelectedLetters() = runTest(testDispatcher) {
        val vm = createViewModelWithOrderMatch(testScheduler)

        val quiz = vm.state.value.currentQuiz!!
        val firstPoolIndex = 0
        vm.handleIntent(QuizIntent.SelectPoolLetter(firstPoolIndex))

        assertEquals(1, vm.state.value.selectedLetters.size)
        assertEquals(quiz.charPool[firstPoolIndex], vm.state.value.selectedLetters[0])
        assertTrue(firstPoolIndex in vm.state.value.usedPoolIndices)
    }

    /** ORDER_MATCH: 선택한 글자를 되돌리면 selectedLetters에서 제거되는지 검증 */
    @Test
    fun orderMatch_undoSelectedLetter_removesFromSelectedLetters() = runTest(testDispatcher) {
        val vm = createViewModelWithOrderMatch(testScheduler)

        vm.handleIntent(QuizIntent.SelectPoolLetter(0))
        vm.handleIntent(QuizIntent.SelectPoolLetter(1))
        assertEquals(2, vm.state.value.selectedLetters.size)

        vm.handleIntent(QuizIntent.UndoSelectedLetter(0))
        assertEquals(1, vm.state.value.selectedLetters.size)
    }

    /** 콤보 2 이상일 때 XP 보너스가 (combo-1) 만큼 추가되는지 검증 (콤보 보상 공식) */
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
