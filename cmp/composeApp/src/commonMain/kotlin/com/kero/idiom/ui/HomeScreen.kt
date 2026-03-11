package com.kero.idiom.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kero.idiom.core.components.IdiomDivider
import com.kero.idiom.core.components.IdiomTab
import com.kero.idiom.core.components.IdiomTabBar
import com.kero.idiom.core.theme.*
import com.kero.idiom.domain.model.Idiom
import com.kero.idiom.domain.model.UserStats
import com.kero.idiom.domain.repository.IdiomRepository
import com.kero.idiom.domain.usecase.GetUserStatsUseCase
import org.koin.compose.koinInject

@Composable
fun HomeScreen(
    onTabSelected: (IdiomTab) -> Unit,
    onStartQuiz: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val repository: IdiomRepository = koinInject()
    val getUserStatsUseCase: GetUserStatsUseCase = koinInject()
    
    var todayIdiom by remember { mutableStateOf<Idiom?>(null) }
    val userStats by getUserStatsUseCase().collectAsState(UserStats())

    val infiniteTransition = rememberInfiniteTransition(label = "breathing")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))

            // 1️⃣ [상단] 슬림해진 나의 칭호 및 상태 바
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(BgDark)
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "현재 나의 칭호",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.5f),
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = userStats.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CompactStatItem("${userStats.currentStreak}일", "연속")
                    Spacer(Modifier.width(16.dp))
                    CompactStatItem("Lv.${userStats.level}", "단계")
                }
            }

            Spacer(Modifier.height(48.dp))

            // 2️⃣ [중앙] 압도적인 강독 시작 버튼 (Breathing Action)
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    }
                    .shadow(elevation = 16.dp, shape = CircleShape)
                    .clip(CircleShape)
                    .background(BgDark)
                    .clickable(onClick = onStartQuiz),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "오늘의",
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.7f),
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "강독 시작",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "→",
                        fontSize = 20.sp,
                        color = Color.White
                    )
                }
            }

            Spacer(Modifier.height(56.dp))

            // 3️⃣ [하단] 오늘의 성어 (부드러운 지식 카드)
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "오늘의 지혜",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    modifier = Modifier.fillMaxWidth()
                )
                
                todayIdiom?.let { idiom ->
                    TodayIdiomCard(idiom)
                } ?: Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(BgSurface),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = BgDark, modifier = Modifier.size(24.dp))
                }
            }

            Spacer(Modifier.height(32.dp))
        }

        IdiomTabBar(
            selectedTab = IdiomTab.Home,
            onTabSelected = onTabSelected
        )
    }
}

@Composable
private fun CompactStatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.End) {
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = label,
            fontSize = 10.sp,
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = idiom.hanja,
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary,
                modifier = Modifier.weight(1f)
            )
            TagChip("고사성어")
        }
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
