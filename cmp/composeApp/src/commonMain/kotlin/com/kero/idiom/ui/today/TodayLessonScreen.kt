package com.kero.idiom.ui.today

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kero.idiom.core.theme.BgPrimary
import com.kero.idiom.core.theme.CorrectGreen
import com.kero.idiom.core.theme.TextMuted
import com.kero.idiom.core.theme.TextPrimary
import com.kero.idiom.core.util.DateUtils
import com.kero.idiom.domain.model.TodayLesson
import com.kero.idiom.domain.model.TodayLessonQuestion
import com.kero.idiom.openNotificationSettings
import com.kero.idiom.rememberStudyReminderPermissionRequest
import com.kero.idiom.stripHtml
import com.kero.idiom.ui.weeklyStudyLabel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

private val LessonPaper = Color(0xFFF8F2E6)
private val LessonInk = Color(0xFF332D23)
private val LessonSeal = Color(0xFF8C3F35)
private val LessonShape = RoundedCornerShape(14.dp)
private val ButtonInsets = PaddingValues(horizontal = 16.dp, vertical = 12.dp)

@Composable
internal fun TodayLessonScreen(day: Long, onBack: () -> Unit, onCollection: () -> Unit) {
    val viewModel: TodayLessonViewModel = koinViewModel { parametersOf(day) }
    val state by viewModel.state.collectAsState()
    val scroll = rememberScrollState()
    val lesson = state.lesson

    LaunchedEffect(lesson?.currentIndex) { scroll.scrollTo(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgPrimary)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(scroll)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        TextButton(
            onClick = onBack,
            modifier = Modifier.heightIn(min = 48.dp)
        ) {
            Text("서당으로", style = MaterialTheme.typography.bodyLarge, color = TextPrimary)
        }
        Text("오늘의 서당", modifier = Modifier.semantics { heading() }, style = MaterialTheme.typography.headlineLarge)
        LessonLabel(DateUtils.getDateStringDaysAgo((currentLessonDay() - day).toInt()))

        state.error?.let { message ->
            LessonCard {
                Text(message, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.semantics {
                    liveRegion = LiveRegionMode.Polite
                })
                LessonButton("다시 시도", viewModel::retry, enabled = !state.busy, primary = false)
            }
        }

        when {
            lesson == null -> {
                if (state.busy) CircularProgressIndicator(color = LessonInk)
            }
            lesson.isResultVisible -> LessonResult(lesson, state, viewModel::enableReminder, onCollection, onBack)
            else -> {
                val index = lesson.currentIndex
                LessonQuestion(
                    question = lesson.questions[index],
                    index = index,
                    total = lesson.questions.size,
                    enabled = !state.busy && state.error == null,
                    onHint = { viewModel.revealHint(index) },
                    onAnswer = { viewModel.answer(index, it) },
                    onNext = { viewModel.advance(index) }
                )
            }
        }
    }
}

@Composable
private fun LessonQuestion(
    question: TodayLessonQuestion,
    index: Int,
    total: Int,
    enabled: Boolean,
    onHint: () -> Unit,
    onAnswer: (String) -> Unit,
    onNext: () -> Unit
) {
    LessonLabel("${index + 1} / $total · ${if (question.isReview) "복습" else "새 문제"}")
    LessonCard(modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }) {
        Text(
            text = question.prompt.stripHtml(),
            color = TextPrimary,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.semantics { heading() }
        )
        Text("어울리는 성어를 골라 보세요.", style = MaterialTheme.typography.bodyLarge, color = TextMuted)
    }
    if (question.selectedAnswer == null) {
        question.options.forEach { option ->
            LessonButton(option, { onAnswer(option) }, enabled = enabled, primary = false)
        }
        if (question.isHintRevealed) {
            LessonCard {
                LessonLabel("힌트")
                Text(
                    question.hint.stripHtml(),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                )
            }
        } else {
            LessonButton("무료 힌트 보기", onHint, enabled = enabled, primary = false)
        }
    } else {
        LessonCard(modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }) {
            val correct = question.isCorrect == true
            LessonLabel(
                text = if (correct) "맞았어요" else "다음에 다시 만나요",
                color = if (correct) CorrectGreen else LessonSeal
            )
            Text("${question.idiom.word} · ${question.idiom.hanja}", style = MaterialTheme.typography.headlineMedium)
            Text(question.idiom.meaning.stripHtml(), style = MaterialTheme.typography.bodyLarge)
            if (!correct) {
                Text("고른 답 · ${question.selectedAnswer}", style = MaterialTheme.typography.bodyLarge, color = TextMuted)
            }
        }
        LessonButton(if (index + 1 == total) "오늘의 기록 보기" else "다음 문제", onNext, enabled)
    }
}

@Composable
private fun LessonResult(
    lesson: TodayLesson,
    state: TodayLessonState,
    onEnableReminder: () -> Unit,
    onCollection: () -> Unit,
    onBack: () -> Unit
) {
    LessonCard(modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }) {
        LessonLabel("오늘의 공부 완료")
        Text(
            "서첩에 도장을 찍었어요",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.semantics { heading() }
        )
        Text(weeklyStudyLabel(state.completedDays), style = MaterialTheme.typography.bodyLarge)
        Text("${lesson.questions.size}문제 중 ${lesson.correctCount}문제 정답", style = MaterialTheme.typography.bodyLarge)
    }

    if (lesson.newlyCollected.isEmpty()) {
        Text("오늘 푼 성어를 기록했어요.", style = MaterialTheme.typography.bodyLarge)
    } else {
        LessonCard {
            LessonLabel("서고에 새로 담은 성어")
            lesson.newlyCollected.forEach { idiom ->
                Text("${idiom.word} · ${idiom.hanja}", style = MaterialTheme.typography.bodyLarge)
            }
        }
        LessonButton("서고에서 보기", onCollection)
    }

    if (lesson.questions.any { it.isCorrect == false || it.isHintRevealed }) {
        Text("헷갈린 성어는 복습에 담았어요.", style = MaterialTheme.typography.bodyLarge)
    }

    if (state.firstCompletion && !state.reminderEnabled) {
        var dismissed by rememberSaveable { mutableStateOf(false) }
        var denied by rememberSaveable { mutableStateOf(false) }
        if (!dismissed) {
            val request = rememberStudyReminderPermissionRequest { granted ->
                denied = !granted
                if (granted) onEnableReminder()
            }
            LessonCard {
                Text("공부할 시간을 알려드릴까요?", style = MaterialTheme.typography.bodyLarge)
                Text("아직 오늘의 문제를 풀지 않은 날에 알려드려요.", style = MaterialTheme.typography.bodyLarge, color = TextMuted)
                if (denied) {
                    Text("기기에서 알림을 허용한 뒤 다시 켤 수 있어요.", style = MaterialTheme.typography.bodyLarge)
                    LessonButton("기기 알림 설정", ::openNotificationSettings, primary = false)
                }
                LessonButton("학습 알림 켜기", request, enabled = !state.busy, primary = false)
                LessonButton("나중에", { dismissed = true }, primary = false)
            }
        }
    }
    LessonButton("서당으로 돌아가기", onBack, primary = false)
}

@Composable
internal fun TodayLessonUnavailable(message: String, onRetry: () -> Unit) {
    Box(Modifier.fillMaxSize().background(BgPrimary).padding(24.dp), contentAlignment = Alignment.Center) {
        LessonCard {
            Text(message, style = MaterialTheme.typography.headlineMedium)
            LessonButton("다시 시도", onRetry)
        }
    }
}

@Composable
internal fun LessonCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Surface(modifier.fillMaxWidth(), shape = LessonShape, color = LessonPaper, contentColor = TextPrimary) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp), content = content)
    }
}

@Composable
internal fun LessonLabel(text: String, color: Color = LessonSeal) {
    Text(text, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = color)
}

@Composable
internal fun LessonButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    primary: Boolean = true
) {
    val modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)
    val label: @Composable () -> Unit = {
        Text(text, style = MaterialTheme.typography.labelLarge.copy(fontSize = 20.sp, lineHeight = 27.sp))
    }
    if (primary) {
        Button(
            onClick, modifier, enabled, shape = LessonShape,
            contentPadding = ButtonInsets,
            colors = ButtonDefaults.buttonColors(containerColor = LessonInk, contentColor = Color.White)
        ) { label() }
    } else {
        OutlinedButton(
            onClick, modifier, enabled, shape = LessonShape,
            contentPadding = ButtonInsets,
            colors = ButtonDefaults.outlinedButtonColors(containerColor = BgPrimary, contentColor = LessonInk)
        ) { label() }
    }
}
