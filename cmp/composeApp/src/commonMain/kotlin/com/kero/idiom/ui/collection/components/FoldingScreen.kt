package com.kero.idiom.ui.collection.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kero.idiom.core.theme.BgDark
import com.kero.idiom.core.theme.BorderColor
import com.kero.idiom.core.theme.HintBg
import com.kero.idiom.ui.collection.model.FoldingScreenPanelData
import com.kero.idiom.ui.collection.model.PanelType

@Composable
fun FoldingScreenScrollable(
    panels: List<FoldingScreenPanelData>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "나의 정진 병풍",
            style = MaterialTheme.typography.titleMedium,
            color = BgDark,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(end = 24.dp)
        ) {
            items(panels) { panel ->
                FoldingScreenPanel(panel)
            }
        }
    }
}

@Composable
fun FoldingScreenPanel(
    data: FoldingScreenPanelData,
    modifier: Modifier = Modifier
) {
    val animatedProgress = remember { Animatable(0f) }
    
    LaunchedEffect(data.progress) {
        animatedProgress.animateTo(
            targetValue = data.progress,
            animationSpec = tween(durationMillis = 1500)
        )
    }

    Column(
        modifier = modifier
            .width(140.dp)
            .height(200.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(HintBg)
            .border(1.dp, BorderColor, RoundedCornerShape(4.dp))
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 수묵화 드로잉 영역 (Canvas)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val height = size.height
                
                // 추상화된 수묵화 요소 드로잉 (Progress에 따라 선명도 조절)
                val inkAlpha = animatedProgress.value.coerceIn(0.1f, 1f)
                val inkColor = BgDark.copy(alpha = inkAlpha)
                
                when (data.type) {
                    PanelType.MOUNTAIN -> drawMountain(width, height, inkColor)
                    PanelType.FOREST -> drawForest(width, height, inkColor)
                    PanelType.WATER -> drawWater(width, height, inkColor)
                    PanelType.CRANE -> drawCrane(width, height, inkColor)
                }
            }
        }
        
        Spacer(Modifier.height(8.dp))
        
        // 하단 캡션
        Text(
            text = data.title,
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            ),
            color = BgDark.copy(alpha = 0.7f)
        )
    }
}

// --- 수묵화 드로잉 헬퍼 (Jenny's Ink Logic) ---

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawMountain(
    width: Float, height: Float, color: Color
) {
    val path = Path().apply {
        moveTo(0f, height * 0.8f)
        quadraticBezierTo(width * 0.3f, height * 0.2f, width * 0.6f, height * 0.5f)
        quadraticBezierTo(width * 0.8f, height * 0.1f, width, height * 0.7f)
    }
    drawPath(path, color, style = Stroke(width = 2.dp.toPx()))
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawForest(
    width: Float, height: Float, color: Color
) {
    // 소나무 형상화 (간략하게)
    for (i in 0..2) {
        val x = width * (0.2f + i * 0.3f)
        drawLine(color, Offset(x, height * 0.9f), Offset(x, height * 0.5f), strokeWidth = 3.dp.toPx())
        drawCircle(color, radius = 15.dp.toPx(), center = Offset(x, height * 0.5f))
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawWater(
    width: Float, height: Float, color: Color
) {
    for (i in 0..2) {
        val y = height * (0.4f + i * 0.2f)
        val path = Path().apply {
            moveTo(width * 0.2f, y)
            quadraticBezierTo(width * 0.5f, y + 10.dp.toPx(), width * 0.8f, y)
        }
        drawPath(path, color, style = Stroke(width = 1.5.dp.toPx()))
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawCrane(
    width: Float, height: Float, color: Color
) {
    // 학/구름 형상화
    drawCircle(color.copy(alpha = 0.3f), radius = 25.dp.toPx(), center = Offset(width * 0.7f, height * 0.3f))
    val wingPath = Path().apply {
        moveTo(width * 0.3f, height * 0.4f)
        lineTo(width * 0.5f, height * 0.35f)
        lineTo(width * 0.4f, height * 0.45f)
    }
    drawPath(wingPath, color)
}
