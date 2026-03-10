package com.kero.idiom.feature.result.viewmodel

import androidx.lifecycle.ViewModel
import com.kero.idiom.feature.result.contract.ResultIntent
import com.kero.idiom.feature.result.contract.ResultSideEffect
import com.kero.idiom.feature.result.contract.ResultState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

class ResultViewModel : ViewModel() {

    private val _state = MutableStateFlow(ResultState())
    val state: StateFlow<ResultState> = _state.asStateFlow()

    private val _sideEffect = Channel<ResultSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    fun handleIntent(intent: ResultIntent) {
        // 결과 화면에서 추가 로직이 필요하면 여기에 작성
    }
}
