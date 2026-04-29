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

    @Test
    fun init_defaultState() {
        val vm = ResultViewModel()
        val state = vm.state.value

        assertEquals(0, state.score)
        assertEquals(0, state.xpGained)
        assertEquals(false, state.isLoading)
    }

    @Test
    fun init_setsScoreAndXp() {
        val vm = ResultViewModel()

        vm.handleIntent(ResultIntent.Init(score = 4, xpGained = 25))

        assertEquals(4, vm.state.value.score)
        assertEquals(25, vm.state.value.xpGained)
    }

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

    @Test
    fun init_multipleInits_overwritesState() {
        val vm = ResultViewModel()

        vm.handleIntent(ResultIntent.Init(score = 3, xpGained = 10))
        vm.handleIntent(ResultIntent.Init(score = 5, xpGained = 30))

        assertEquals(5, vm.state.value.score)
        assertEquals(30, vm.state.value.xpGained)
    }
}
