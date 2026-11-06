package com.kero.idiom.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kero.idiom.core.components.IdiomTab
import com.kero.idiom.core.components.IdiomTabBar
import com.kero.idiom.core.theme.BgPrimary
import com.kero.idiom.core.theme.TextMuted
import com.kero.idiom.core.theme.TextPrimary
import com.kero.idiom.ui.home.HomeViewModel
import com.kero.idiom.ui.today.LessonButton
import com.kero.idiom.ui.today.LessonCard
import com.kero.idiom.ui.today.LessonLabel
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    onTabSelected: (IdiomTab) -> Unit,
    onStartQuiz: () -> Unit,
    onOpenTodayLesson: () -> Unit
) {
    val viewModel: HomeViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()
    val stats = state.userStats

    LaunchedEffect(Unit) {
        viewModel.refreshLesson()
        while (true) {
            delay(30_000)
            viewModel.refreshLesson(ifDateChanged = true)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgPrimary)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text("서당", style = MaterialTheme.typography.headlineLarge, color = TextPrimary)

            LessonCard {
                LessonLabel("오늘의 서당")
                state.dateLabel?.let { date -> Text(date, color = TextMuted, style = MaterialTheme.typography.bodyLarge) }
                Text(
                    text = state.lessonTheme ?: "일상에서 만나는 사자성어",
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary
                )
                Text(
                    text = when {
                        state.completed -> "오늘 공부를 마쳤어요."
                        state.started -> "3문제 중 ${state.answeredCount}문제 답했어요."
                        else -> "객관식 3문제 · 약 1분"
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextMuted
                )
                LessonButton(
                    text = when {
                        state.completed -> "오늘의 기록 보기"
                        state.started -> "이어서 풀기"
                        else -> "오늘의 3문제 풀기"
                    },
                    onClick = onOpenTodayLesson
                )
                state.completedDays?.let { days ->
                    Text(
                        text = weeklyStudyLabel(days),
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextMuted
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("자유 연습", style = MaterialTheme.typography.headlineMedium, color = TextPrimary)
                Text(
                    "다양한 유형으로 사자성어 5문제를 풀어 보세요.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextMuted
                )
                LessonButton("자유 연습 시작", onStartQuiz, primary = false)
            }

            Text(
                text = "Lv.${stats.level} · ${stats.title} · 연속 ${stats.currentStreak}일",
                style = MaterialTheme.typography.bodyLarge,
                color = TextMuted
            )
        }

        IdiomTabBar(selectedTab = IdiomTab.Home, onTabSelected = onTabSelected)
    }
}

internal fun weeklyStudyLabel(days: Int): String =
    if (days >= 3) "이번 주 ${days}일 공부 · 목표 달성" else "이번 주 ${days} / 3일 공부"
