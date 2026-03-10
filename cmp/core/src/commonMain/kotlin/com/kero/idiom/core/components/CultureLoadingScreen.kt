package com.kero.idiom.core.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kero.idiom.core.theme.BgDark

/**
 * 어르신들을 위한 동양적인 느낌의 인트로/로딩 화면.
 * 한지 배경 위에 먹물이 은은하게 번지는 애니메이션을 보여줍니다.
 */
@Composable
fun CultureLoadingScreen(
    modifier: Modifier = Modifier,
    message: String = "서책을 펼쳐 준비하고 있습니다..."
) {
    val infiniteTransition = rememberInfiniteTransition(label = "inkSpread")
    
    // 먹물 번짐 효과를 위한 애니메이션 값
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "inkAlpha"
    )
    
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "inkScale"
    )

    HanjiBackground(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // 배경에 은은하게 깔리는 큰 먹물 점 연출
            Canvas(modifier = Modifier.size(300.dp)) {
                drawCircle(
                    color = Color.Black.copy(alpha = alpha * 0.1f),
                    radius = size.minDimension / 2 * scale
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // 상징적인 원형 아이콘 (먹물 한 방울 느낌)
                Canvas(modifier = Modifier.size(60.dp)) {
                    drawCircle(
                        color = BgDark.copy(alpha = alpha),
                        radius = size.minDimension / 2
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = message,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 1.sp
                    ),
                    color = BgDark.copy(alpha = 0.7f),
                    modifier = Modifier.graphicsLayer {
                        this.alpha = alpha
                    }
                )
            }
        }
    }
}
