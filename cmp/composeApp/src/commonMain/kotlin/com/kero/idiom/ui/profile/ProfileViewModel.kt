package com.kero.idiom.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kero.idiom.domain.repository.UserStatsRepository
import com.kero.idiom.domain.usecase.GetUserStatsUseCase
import com.kero.idiom.ui.profile.contract.ProfileIntent
import com.kero.idiom.ui.profile.contract.ProfileState
import com.kero.idiom.updateReminderSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getUserStatsUseCase: GetUserStatsUseCase,
    private val userStatsRepository: UserStatsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    init {
        observeStats()
    }

    fun onIntent(intent: ProfileIntent) {
        when (intent) {
            is ProfileIntent.ToggleNotification -> {
                viewModelScope.launch {
                    userStatsRepository.updateNotificationEnabled(intent.enabled)
                    updateReminderSettings(intent.enabled)
                }
            }
            ProfileIntent.Refresh -> observeStats()
        }
    }

    private fun observeStats() {
        getUserStatsUseCase()
            .onEach { stats ->
                _state.update { it.copy(userStats = stats) }
            }
            .launchIn(viewModelScope)
    }
}
