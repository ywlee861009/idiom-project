package com.kero.idiom.feature.result.viewmodel

import app.cash.turbine.test
import com.kero.idiom.feature.result.contract.ResultIntent
import com.kero.idiom.feature.result.contract.ResultSideEffect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** 퀴즈 결과 화면(ResultViewModel)의 상태 관리 및 화면 전환 로직 검증 */
@OptIn(ExperimentalCoroutinesApi::class)
class ResultViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /** ViewModel 생성 시 ��수 0, XP 0, 로딩 false 등 초�� 상태가 올바른지 검증 */
    @Test
    fun init_defaultState() {
        val vm = ResultViewModel()
        val state = vm.state.value

        assertEquals(0, state.score)
        assertEquals(0, state.xpGained)
        assertEquals(false, state.isLoading)
    }

    /** Init Intent로 점수와 XP가 상태에 정확히 반영되는지 검증 */
    @Test
    fun init_setsScoreAndXp() {
        val vm = ResultViewModel()

        vm.handleIntent(ResultIntent.Init(score = 4, xpGained = 25))

        assertEquals(4, vm.state.value.score)
        assertEquals(25, vm.state.value.xpGained)
    }

    /** "다시하기" 클릭 시 퀴즈 화면으�� 네비게이션 SideEffect가 발생하는지 검증 */
    @Test
    fun onRetryClick_navigatesToQuiz() = runTest {
        val vm = ResultViewModel()

        vm.sideEffect.test {
            vm.handleIntent(ResultIntent.OnRetryClick)
            advanceUntilIdle()

            val effect = awaitItem()
            assertTrue(effect is ResultSideEffect.NavigateToQuiz)
        }
    }

    /** "홈으로" 클릭 시 홈 화면으로 네비게이션 SideEffect가 발생하는지 검증 */
    @Test
    fun onHomeClick_navigatesToHome() = runTest {
        val vm = ResultViewModel()

        vm.sideEffect.test {
            vm.handleIntent(ResultIntent.OnHomeClick)
            advanceUntilIdle()

            val effect = awaitItem()
            assertTrue(effect is ResultSideEffect.NavigateToHome)
        }
    }

    /** Init을 여러 번 호출해도 마지막 값으로 덮어쓰기되는지 검증 (중복 호출 안전성) */
    @Test
    fun init_multipleInits_overwritesState() {
        val vm = ResultViewModel()

        vm.handleIntent(ResultIntent.Init(score = 3, xpGained = 10))
        vm.handleIntent(ResultIntent.Init(score = 5, xpGained = 30))

        assertEquals(5, vm.state.value.score)
        assertEquals(30, vm.state.value.xpGained)
    }
}
