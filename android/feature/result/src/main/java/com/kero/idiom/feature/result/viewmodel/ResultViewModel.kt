package com.kero.idiom.feature.result.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kero.idiom.feature.result.contract.ResultIntent
import com.kero.idiom.feature.result.contract.ResultSideEffect
import com.kero.idiom.feature.result.contract.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(ResultState())
    val state = _state.asStateFlow()

    private val _effect = Channel<ResultSideEffect>()
    val effect = _effect.receiveAsFlow()

    fun processIntent(intent: ResultIntent) {
        when (intent) {
            is ResultIntent.Init -> {
                _state.update { it.copy(score = intent.score) }
            }
            is ResultIntent.OnRetryClick -> {
                viewModelScope.launch {
                    _effect.send(ResultSideEffect.NavigateToQuiz)
                }
            }
            is ResultIntent.OnHomeClick -> {
                viewModelScope.launch {
                    _effect.send(ResultSideEffect.NavigateToHome)
                }
            }
        }
    }
}
