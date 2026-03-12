package com.kero.idiom.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kero.idiom.core.components.IdiomTab
import com.kero.idiom.core.components.IdiomTabBar
import com.kero.idiom.core.theme.*
import com.kero.idiom.domain.model.Idiom
import com.kero.idiom.domain.repository.IdiomRepository
import com.kero.idiom.openBrowser
import org.koin.compose.koinInject

@Composable
fun CollectionScreen(
    onTabSelected: (IdiomTab) -> Unit
) {
    val repository: IdiomRepository = koinInject()
    var acquiredIdioms by remember { mutableStateOf<List<Idiom>>(emptyList()) }
    var totalIdiomsCount by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        acquiredIdioms = repository.getAcquiredIdioms()
        totalIdiomsCount = repository.getAllIdioms().size
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
        ) {
            Spacer(Modifier.height(8.dp))

            // 타이틀 및 진행률
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "서고",
                        style = MaterialTheme.typography.headlineLarge,
                        color = TextPrimary
                    )
                    Text(
                        text = "서책 수집 현황 (${acquiredIdioms.size}/${totalIdiomsCount})",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextMuted
                    )
                }

                // 진행률 바 (The Calm Ink 스타일)
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    val progress = if (totalIdiomsCount > 0) {
                        acquiredIdioms.size.toFloat() / totalIdiomsCount.toFloat()
                    } else 0f
                    
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = BgDark,
                        trackColor = BorderColor,
                    )
                    
                    Text(
                        text = "전체 중 ${(progress * 100).toInt()}% 완료",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // 획득한 카드 섹션 헤더
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "나의 서첩",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                Text(
                    text = "${acquiredIdioms.size}권",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
            }

            Spacer(Modifier.height(12.dp))

            if (acquiredIdioms.isEmpty()) {
                // 빈 상태
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("📖", fontSize = 48.sp)
                        Text(
                            text = "아직 획득한 성어가 없어요",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary
                        )
                        Text(
                            text = "퀴즈에서 정답을 맞히면\n성어 카드를 획득할 수 있어요",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMuted,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            } else {
                // 카드 그리드
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(acquiredIdioms) { idiom ->
                        IdiomCollectionCard(idiom)
                    }
                    item { Spacer(Modifier.height(16.dp)) }
                    item { Spacer(Modifier.height(16.dp)) }
                }
            }
        }

        IdiomTabBar(
            selectedTab = IdiomTab.Study,
            onTabSelected = onTabSelected
        )
    }
}

@Composable
private fun IdiomCollectionCard(idiom: Idiom) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(BgDark)
            .clickable { 
                val searchUrl = "https://hanja.dict.naver.com/search?query=${idiom.word}"
                openBrowser(searchUrl)
            }
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = idiom.hanja,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            letterSpacing = 2.sp
        )
        Text(
            text = idiom.word,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.6f)
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = idiom.meaning,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.75f),
            lineHeight = 18.sp
        )
        Spacer(Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "상세 풀이 보기(네이버)",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.5f)
            )
            Text(
                text = "획득 완료 ✓",
                fontSize = 11.sp,
                color = Color.White.copy(alpha = 0.4f)
            )
        }
    }
}
