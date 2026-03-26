package com.kero.idiom.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kero.idiom.core.util.Logger
import com.kero.idiom.domain.repository.IdiomRepository
import com.kero.idiom.domain.usecase.GetUserStatsUseCase
import com.kero.idiom.ui.home.contract.HomeIntent
import com.kero.idiom.ui.home.contract.HomeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getUserStatsUseCase: GetUserStatsUseCase,
    private val idiomRepository: IdiomRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        observeStats()
        loadTodayIdiom()
    }

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.LoadTodayIdiom -> loadTodayIdiom()
            HomeIntent.Refresh -> {
                observeStats()
                loadTodayIdiom()
            }
        }
    }

    private fun observeStats() {
        getUserStatsUseCase()
            .onEach { stats ->
                _state.update { it.copy(userStats = stats) }
            }
            .launchIn(viewModelScope)
    }

    private fun loadTodayIdiom() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val idiom = idiomRepository.getRandomIdioms(1).firstOrNull()
                _state.update { it.copy(todayIdiom = idiom, isLoading = false) }
            } catch (e: Exception) {
                Logger.d("오늘의 성어 로드 실패: ${e.message}")
                _state.update { it.copy(isLoading = false) }
            }
        }
    }
}
