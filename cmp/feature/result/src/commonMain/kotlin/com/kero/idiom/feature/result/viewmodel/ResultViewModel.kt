package com.kero.idiom.feature.result.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kero.idiom.feature.result.contract.ResultIntent
import com.kero.idiom.feature.result.contract.ResultSideEffect
import com.kero.idiom.feature.result.contract.ResultState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ResultViewModel : ViewModel() {

    private val _state = MutableStateFlow(ResultState())
    val state: StateFlow<ResultState> = _state.asStateFlow()

    private val _sideEffect = Channel<ResultSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    fun handleIntent(intent: ResultIntent) {
        when (intent) {
            is ResultIntent.Init -> {
                _state.value = _state.value.copy(
                    score = intent.score,
                    xpGained = intent.xpGained
                )
            }
            ResultIntent.OnRetryClick -> {
                viewModelScope.launch {
                    _sideEffect.send(ResultSideEffect.NavigateToQuiz)
                }
            }
            ResultIntent.OnHomeClick -> {
                viewModelScope.launch {
                    _sideEffect.send(ResultSideEffect.NavigateToHome)
                }
            }
        }
    }
}
