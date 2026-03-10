package com.kero.idiom.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.kero.idiom.core.theme.BgDark
import com.kero.idiom.core.theme.BgPrimary
import com.kero.idiom.core.theme.BorderColor
import com.kero.idiom.core.theme.IdiomQuizTheme
import com.kero.idiom.core.theme.TextOnDark
import com.kero.idiom.core.theme.TextSecondary

enum class IdiomTab(val label: String, val icon: ImageVector) {
    Home("홈", Icons.Filled.Home),
    Quiz("퀴즈", Icons.Filled.Psychology),
    Dictionary("사전", Icons.Filled.MenuBook)
}

@Composable
fun IdiomTabBar(
    selectedTab: IdiomTab,
    onTabSelected: (IdiomTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(83.dp)
            .padding(start = 21.dp, end = 21.dp, top = 12.dp, bottom = 21.dp)
            .border(1.dp, BorderColor, RoundedCornerShape(36.dp))
            .clip(RoundedCornerShape(36.dp))
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(62.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IdiomTab.entries.forEach { tab ->
                val isSelected = tab == selectedTab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .padding(4.dp)
                        .clip(RoundedCornerShape(26.dp))
                        .background(if (isSelected) BgDark else Color.Transparent)
                        .clickable { onTabSelected(tab) },
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = tab.label,
                            tint = if (isSelected) TextOnDark else TextSecondary
                        )
                        Text(
                            text = tab.label,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 0.5.sp,
                            color = if (isSelected) TextOnDark else TextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun IdiomTabBarPreview() {
    IdiomQuizTheme {
        androidx.compose.material3.Surface(color = BgPrimary) {
            IdiomTabBar(
                selectedTab = IdiomTab.Home,
                onTabSelected = {}
            )
        }
    }
}

@Preview
@Composable
private fun IdiomTabBarQuizPreview() {
    IdiomQuizTheme {
        androidx.compose.material3.Surface(color = BgPrimary) {
            IdiomTabBar(
                selectedTab = IdiomTab.Quiz,
                onTabSelected = {}
            )
        }
    }
}
