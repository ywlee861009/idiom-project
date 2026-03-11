package com.kero.idiom.ui

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kero.idiom.core.components.IdiomTab
import com.kero.idiom.core.components.IdiomTabBar
import com.kero.idiom.core.theme.*
import com.kero.idiom.openFontSizeSettings
import com.kero.idiom.openStorePage
import com.kero.idiom.ui.profile.ProfileViewModel
import com.kero.idiom.ui.profile.contract.ProfileIntent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(
    onTabSelected: (IdiomTab) -> Unit,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val stats = state.userStats

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
                        text = stats.title.first().toString(),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = stats.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Lv.${stats.level} · ${stats.titleDescription}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextMuted
                    )
                }
            }

            // 경험치 및 레벨 진척도 (SeekBar 형태)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(BgSurface)
                    .border(1.dp, BorderColor, RoundedCornerShape(16.dp))
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val currentExp = stats.totalCorrectCount % 10
                val progress = currentExp / 10f
                val remaining = 10 - currentExp

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "현재 ${stats.level}단계 정진 중",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                    Text(
                        text = "다음 단계까지 ${remaining}문제",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted
                    )
                }

                // 시니어 친화적인 두꺼운 프로그레스 바
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .clip(CircleShape)
                        .background(BorderColor)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress)
                            .fillMaxHeight()
                            .clip(CircleShape)
                            .background(BgDark)
                    )
                }
                
                Text(
                    text = "현재 단계에서 ${currentExp}문제를 맞히셨습니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )

                // 다음 타이틀 예고 (명확한 수치 제공!)
                stats.nextTitleInfo?.let { next ->
                    HorizontalDivider(color = BorderColor.copy(alpha = 0.5f), thickness = 1.dp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text("📜", fontSize = 18.sp)
                        Column {
                            val steps = stats.levelsUntilNextTitle
                            Text(
                                text = "앞으로 ${steps}번 더 정진하시면",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextMuted
                            )
                            Text(
                                text = "'${next.title}' 칭호를 얻게 됩니다!",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                    }
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
                    val accuracy = if (stats.totalSolvedCount > 0) {
                        (stats.totalCorrectCount.toFloat() / stats.totalSolvedCount * 100).toInt()
                    } else 0
                    
                    StatCard(value = "${stats.totalSolvedCount}", label = "총 학습 수", modifier = Modifier.weight(1f))
                    StatCard(value = "$accuracy%", label = "정답률", modifier = Modifier.weight(1f))
                    StatCard(value = "${stats.currentStreak}일", label = "연속 학습", modifier = Modifier.weight(1f))
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
                            checked = stats.isNotificationEnabled,
                            onCheckedChange = { viewModel.onIntent(ProfileIntent.ToggleNotification(it)) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = BgDark,
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = BorderColor
                            )
                        )
                    }
                    HorizontalDivider(color = BorderColor, thickness = 1.dp)

                    // 폰트 크기 (시스템 설정으로 이동)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { openFontSizeSettings() }
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
                                text = "폰트 크기 설정",
                                style = MaterialTheme.typography.bodyLarge,
                                color = TextPrimary
                            )
                        }
                        Text(
                            text = "시스템에서 조절",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = BgDark // 더 진한 색으로 강조
                        )
                    }
                    HorizontalDivider(color = BorderColor, thickness = 1.dp)

                    // 앱 버전 (마켓으로 이동)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { openStorePage() }
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
                                text = "업데이트 확인",
                                style = MaterialTheme.typography.bodyLarge,
                                color = TextPrimary
                            )
                        }
                        Text(
                            text = "최신 버전 v2.0.0",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = BgDark // 강조색
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
        }

        IdiomTabBar(
            selectedTab = IdiomTab.Profile,
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
