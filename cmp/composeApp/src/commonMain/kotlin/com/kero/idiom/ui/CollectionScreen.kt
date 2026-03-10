package com.kero.idiom.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import org.koin.compose.koinInject

@Composable
fun CollectionScreen(
    onTabSelected: (IdiomTab) -> Unit
) {
    val repository: IdiomRepository = koinInject()
    var idioms by remember { mutableStateOf<List<Idiom>>(emptyList()) }

    LaunchedEffect(Unit) {
        idioms = repository.getAllIdioms()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgPrimary)
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            // 타이틀
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "서고",
                    style = MaterialTheme.typography.headlineLarge,
                    color = TextPrimary
                )
                Text(
                    text = "획득한 성어 카드 · ${idioms.size}개",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMuted
                )
            }

            Spacer(Modifier.height(20.dp))

            // 획득한 카드 섹션 헤더
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "획득한 카드",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                Text(
                    text = "${idioms.size}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
            }

            Spacer(Modifier.height(12.dp))

            // 카드 그리드
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(idioms) { idiom ->
                    IdiomCollectionCard(idiom)
                }
                item {
                    Spacer(Modifier.height(16.dp))
                }
                item {
                    Spacer(Modifier.height(16.dp))
                }
            }
        }

        // 유료 테마팩 섹션
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "유료 테마팩",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(BgDark)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "PRO",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
            LockedPackCard("조선왕조 실록 팩", "50개 성어 수록", "2,000원")
            LockedPackCard("삼국지 고사성어 팩", "40개 성어 수록", "2,000원")
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
            text = "획득 완료 ✓",
            fontSize = 11.sp,
            color = Color.White.copy(alpha = 0.4f)
        )
    }
}

@Composable
private fun LockedPackCard(title: String, subtitle: String, price: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(BgSurface)
            .border(1.dp, BorderColor, RoundedCornerShape(14.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(BorderColor),
                contentAlignment = Alignment.Center
            ) {
                Text("📖", fontSize = 18.sp)
            }
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
            }
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(BgDark)
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = price,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}
