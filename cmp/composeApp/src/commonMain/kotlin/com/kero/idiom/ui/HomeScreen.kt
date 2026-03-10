package com.kero.idiom.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kero.idiom.core.components.IdiomDivider
import com.kero.idiom.core.components.IdiomTab
import com.kero.idiom.core.components.IdiomTabBar
import com.kero.idiom.core.theme.*
import com.kero.idiom.domain.model.Idiom
import com.kero.idiom.domain.repository.IdiomRepository
import org.koin.compose.koinInject

@Composable
fun HomeScreen(
    onTabSelected: (IdiomTab) -> Unit,
    onStartQuiz: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val repository: IdiomRepository = koinInject()
    var todayIdiom by remember { mutableStateOf<Idiom?>(null) }

    LaunchedEffect(Unit) {
        todayIdiom = repository.getRandomIdioms(1).firstOrNull()
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
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            // 타이틀
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "오늘의 학습",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "서당",
                    style = MaterialTheme.typography.headlineLarge,
                    color = TextPrimary
                )
            }

            // 랭크 카드 (검정 배경)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(BgDark)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "나의 칭호",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.5f),
                        letterSpacing = 0.5.sp
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.White.copy(alpha = 0.15f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Lv.3",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
                Text(
                    text = "초립동이",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White
                )
                Text(
                    text = "오늘도 꾸준히 정진하세요 🎋",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.6f)
                )
                IdiomDivider(
                    modifier = Modifier.padding(vertical = 6.dp),
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    RankStatItem("3", "고사 푼 문제")
                    RankStatItem("12일", "연속 복습")
                    RankStatItem("87%", "정답률")
                }
            }

            // 오늘의 성어 섹션 헤더
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "오늘의 성어",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                Text(
                    text = "더보기",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
            }

            // 오늘의 성어 카드
            todayIdiom?.let { idiom ->
                TodayIdiomCard(idiom)
            } ?: Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(BgSurface),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = BgDark, modifier = Modifier.size(24.dp))
            }

            Spacer(Modifier.height(8.dp))
        }

        // 강독 시작 버튼
        Button(
            onClick = onStartQuiz,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 8.dp)
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BgDark,
                contentColor = TextOnDark
            )
        ) {
            Text(
                text = "강독 시작 →",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        IdiomTabBar(
            selectedTab = IdiomTab.Home,
            onTabSelected = onTabSelected
        )
    }
}

@Composable
private fun RankStatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = Color.White.copy(alpha = 0.5f)
        )
    }
}

@Composable
private fun TodayIdiomCard(idiom: Idiom) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(BgSurface)
            .border(1.dp, BorderColor, RoundedCornerShape(14.dp))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = idiom.hanja,
            style = MaterialTheme.typography.titleLarge,
            color = TextPrimary
        )
        Text(
            text = idiom.word,
            style = MaterialTheme.typography.bodyMedium,
            color = TextMuted,
            letterSpacing = 2.sp
        )
        Text(
            text = idiom.meaning,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )
        Spacer(Modifier.height(4.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TagChip("고사성어")
            TagChip("초급")
        }
    }
}

@Composable
private fun TagChip(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(BorderColor)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            color = TextMuted,
            fontWeight = FontWeight.Medium
        )
    }
}
