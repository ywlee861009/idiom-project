package com.kero.idiom.core.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kero.idiom.core.theme.BgDark
import com.kero.idiom.core.theme.TextOnDark

@Composable
fun CultureLoadingScreen(
    message: String = "하루 다섯 문장, 지혜를 쌓다",
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "dots")

    val alpha1 by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = 0.3f, label = "d1",
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        )
    )
    val alpha2 by infiniteTransition.animateFloat(
        initialValue = 0.6f, targetValue = 1f, label = "d2",
        animationSpec = infiniteRepeatable(
            animation = tween(600, delayMillis = 200, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        )
    )
    val alpha3 by infiniteTransition.animateFloat(
        initialValue = 0.3f, targetValue = 0.8f, label = "d3",
        animationSpec = infiniteRepeatable(
            animation = tween(600, delayMillis = 400, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BgDark)
            .statusBarsPadding()
            .navigationBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text("✒️", fontSize = 32.sp)
            }

            Text(
                text = "사 자 성 어",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = TextOnDark,
                letterSpacing = 8.sp
            )

            Text(
                text = "四 字 成 語",
                fontSize = 18.sp,
                fontWeight = FontWeight.Light,
                color = TextOnDark.copy(alpha = 0.4f),
                letterSpacing = 12.sp
            )

            Spacer(Modifier.height(8.dp))

            // 💡 동적으로 변경되는 문구
            Text(
                text = message,
                fontSize = 16.sp,
                color = TextOnDark.copy(alpha = 0.8f),
                letterSpacing = 1.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(Modifier.height(60.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(TextOnDark.copy(alpha = alpha1))
                )
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(TextOnDark.copy(alpha = alpha2))
                )
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(TextOnDark.copy(alpha = alpha3))
                )
            }
        }
    }
}
