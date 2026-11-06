package com.kero.idiom.ui.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kero.idiom.clearStudyReminder
import com.kero.idiom.core.util.DateUtils
import com.kero.idiom.core.util.Logger
import com.kero.idiom.domain.model.TodayLesson
import com.kero.idiom.domain.repository.TodayLessonRepository
import com.kero.idiom.domain.repository.UserStatsRepository
import com.kero.idiom.updateReminderSettings
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal fun currentLessonDay(): Long = DateUtils.getEpochDay(DateUtils.getCurrentTimeMillis())

internal data class TodayLessonState(
    val lesson: TodayLesson? = null,
    val completedDays: Int = 0,
    val busy: Boolean = true,
    val error: String? = null,
    val firstCompletion: Boolean = false,
    val reminderEnabled: Boolean = false
)

internal class TodayLessonViewModel(
    private val day: Long,
    private val lessons: TodayLessonRepository,
    private val userStats: UserStatsRepository
) : ViewModel() {
    private val _state = MutableStateFlow(TodayLessonState())
    val state = _state.asStateFlow()
    private var retryAction: (() -> Unit)? = null

    init {
        perform("문제를 준비하지 못했어요.") { updateLesson(lessons.start(day)) }
        viewModelScope.launch {
            userStats.getUserStats().collect { stats ->
                _state.update { it.copy(reminderEnabled = stats.isNotificationEnabled) }
            }
        }
    }

    fun revealHint(index: Int) = updateQuestion {
        lessons.revealHint(day, index)
    }

    fun answer(index: Int, choice: String) = updateQuestion {
        lessons.submitAnswer(day, index, choice, completionDay = currentLessonDay())
    }

    fun advance(index: Int) = updateQuestion {
        lessons.advance(day, index)
    }

    fun retry() {
        if (!_state.value.busy) retryAction?.invoke()
    }

    fun enableReminder() {
        if (_state.value.busy) return
        perform("학습 알림을 저장하지 못했어요.") {
            userStats.updateNotificationEnabled(true)
            updateReminderSettings(true)
        }
    }

    private fun updateQuestion(action: suspend () -> TodayLesson) {
        if (_state.value.busy || _state.value.error != null) return
        perform("답을 저장하지 못했어요.") { updateLesson(action()) }
    }

    private suspend fun updateLesson(lesson: TodayLesson) {
        if (lesson.isCompleted && lesson.day == currentLessonDay()) clearStudyReminder()
        if (lesson.isResultVisible) {
            val completedDays = lessons.countCompletedDaysThisWeek(lesson.day)
            val firstCompletion = lessons.isFirstCompletion(lesson.day)
            _state.update { it.copy(lesson = lesson, completedDays = completedDays, firstCompletion = firstCompletion) }
        } else {
            _state.update { it.copy(lesson = lesson) }
        }
    }

    private fun perform(message: String, action: suspend () -> Unit) {
        retryAction = { perform(message, action) }
        _state.update { it.copy(busy = true, error = null) }
        viewModelScope.launch {
            try {
                action()
                retryAction = null
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (error: Exception) {
                Logger.e("Today's lesson operation failed", error)
                _state.update { it.copy(error = message) }
            } finally {
                _state.update { it.copy(busy = false) }
            }
        }
    }
}
