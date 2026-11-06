package com.kero.idiom.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kero.idiom.core.util.DateUtils
import com.kero.idiom.core.util.Logger
import com.kero.idiom.domain.repository.TodayLessonRepository
import com.kero.idiom.domain.usecase.GetUserStatsUseCase
import com.kero.idiom.ui.home.contract.HomeState
import com.kero.idiom.ui.today.currentLessonDay
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    getUserStats: GetUserStatsUseCase,
    private val lessons: TodayLessonRepository
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()
    private var loadedDay: Long? = null
    private var refreshJob: Job? = null

    init {
        viewModelScope.launch {
            getUserStats().collect { stats ->
                _state.update { it.copy(userStats = stats) }
            }
        }
    }

    fun refreshLesson(ifDateChanged: Boolean = false) {
        val day = currentLessonDay()
        if (ifDateChanged && loadedDay == day) return
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            try {
                val dateLabel = DateUtils.getTodayDateString()
                val lesson = lessons.get(day)
                val theme = lesson?.theme ?: lessons.previewTheme(day)
                val completedDays = lessons.countCompletedDaysThisWeek(day)
                _state.update {
                    it.copy(
                        dateLabel = dateLabel,
                        lessonTheme = theme,
                        started = lesson != null && !lesson.isCompleted,
                        answeredCount = lesson?.questions?.count { question -> question.selectedAnswer != null } ?: 0,
                        completed = lesson?.isCompleted == true,
                        completedDays = completedDays
                    )
                }
                loadedDay = day
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (error: Exception) {
                Logger.e("Failed to load today's lesson summary", error)
            }
        }
    }
}
