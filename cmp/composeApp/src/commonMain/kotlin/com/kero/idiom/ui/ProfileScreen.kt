package com.kero.idiom.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kero.idiom.core.components.IdiomTab
import com.kero.idiom.core.components.IdiomTabBar
import com.kero.idiom.core.theme.*

@Composable
fun ProfileScreen(
    onTabSelected: (IdiomTab) -> Unit
) {
    var notificationEnabled by remember { mutableStateOf(true) }

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
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            // 타이틀
            Text(
                text = "내 정보",
                style = MaterialTheme.typography.headlineLarge,
                color = TextPrimary
            )

            // 프로필 카드
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(BgSurface)
                    .border(1.dp, BorderColor, RoundedCornerShape(16.dp))
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(BgDark),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "初",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "초립동이",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Lv.3 · 성어 수집가 입문",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextMuted
                    )
                }
            }

            // 학습 통계
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "학습 통계",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    letterSpacing = 0.5.sp
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(value = "132", label = "총 학습 수", modifier = Modifier.weight(1f))
                    StatCard(value = "87%", label = "정답률", modifier = Modifier.weight(1f))
                    StatCard(value = "12일", label = "연속 학습", modifier = Modifier.weight(1f))
                }
            }

            // 설정
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "설정",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    letterSpacing = 0.5.sp
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(BgSurface)
                        .border(1.dp, BorderColor, RoundedCornerShape(14.dp))
                ) {
                    // 알림 설정
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text("🔔", fontSize = 16.sp)
                            Text(
                                text = "알림 설정",
                                style = MaterialTheme.typography.bodyLarge,
                                color = TextPrimary
                            )
                        }
                        Switch(
                            checked = notificationEnabled,
                            onCheckedChange = { notificationEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = BgDark,
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = BorderColor
                            )
                        )
                    }
                    Divider(color = BorderColor, thickness = 1.dp)

                    // 폰트 크기
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text("T", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text(
                                text = "폰트 크기",
                                style = MaterialTheme.typography.bodyLarge,
                                color = TextPrimary
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "보통",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextMuted
                            )
                            Text("›", fontSize = 16.sp, color = TextMuted)
                        }
                    }
                    Divider(color = BorderColor, thickness = 1.dp)

                    // 앱 버전
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text("ℹ", fontSize = 16.sp, color = TextPrimary)
                            Text(
                                text = "앱 버전",
                                style = MaterialTheme.typography.bodyLarge,
                                color = TextPrimary
                            )
                        }
                        Text(
                            text = "v2.0.0",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMuted
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
        }

        IdiomTabBar(
            selectedTab = IdiomTab.Home,
            onTabSelected = onTabSelected
        )
    }
}

@Composable
private fun StatCard(value: String, label: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(BgPrimary)
            .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.displaySmall,
            color = TextPrimary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
            letterSpacing = 0.5.sp
        )
    }
}
