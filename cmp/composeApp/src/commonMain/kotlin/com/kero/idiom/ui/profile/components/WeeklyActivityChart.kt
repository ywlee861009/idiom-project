package com.kero.idiom.ui.profile.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kero.idiom.core.theme.*
import com.kero.idiom.core.util.DateUtils
import com.kero.idiom.domain.model.DailyRecord

@Composable
fun WeeklyActivityChart(
    weeklyRecords: List<DailyRecord>,
    modifier: Modifier = Modifier
) {
    val maxSolved = (weeklyRecords.maxOfOrNull { it.solvedCount } ?: 0).coerceAtLeast(5)
    val totalSolved = weeklyRecords.sumOf { it.solvedCount }
    val totalCorrect = weeklyRecords.sumOf { it.correctCount }
    val weeklyAccuracy = if (totalSolved > 0) (totalCorrect.toFloat() / totalSolved * 100).toInt() else 0
    val activeDays = weeklyRecords.count { it.solvedCount > 0 }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(BgSurface)
            .border(1.dp, BorderColor, RoundedCornerShape(16.dp))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 헤더
        Text(
            text = "이번 주 학습",
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimary
        )

        // 요약 수치
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            WeeklySummaryItem(value = "$totalSolved", label = "풀이 수")
            WeeklySummaryItem(value = "$weeklyAccuracy%", label = "정답률")
            WeeklySummaryItem(value = "${activeDays}일", label = "학습일")
        }

        // 막대 그래프
        BarChart(
            records = weeklyRecords,
            maxValue = maxSolved,
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
        )
    }
}

@Composable
private fun WeeklySummaryItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = TextMuted
        )
    }
}

@Composable
private fun BarChart(
    records: List<DailyRecord>,
    maxValue: Int,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()
    val barColor = BgDark
    val guidelineColor = BorderColor
    val labelStyle = TextStyle(
        fontSize = 11.sp,
        color = TextMuted
    )
    val valueStyle = TextStyle(
        fontSize = 10.sp,
        color = TextSecondary,
        fontWeight = FontWeight.Medium
    )
    val todayLabelStyle = TextStyle(
        fontSize = 11.sp,
        color = TextPrimary,
        fontWeight = FontWeight.Bold
    )

    val dayLabels = records.map { DateUtils.getDayOfWeekLabel(it.date) }

    Canvas(modifier = modifier) {
        val bottomPadding = 24.dp.toPx()
        val topPadding = 16.dp.toPx()
        val chartHeight = size.height - bottomPadding - topPadding
        val barCount = records.size
        val totalBarWidth = size.width / barCount
        val barWidth = totalBarWidth * 0.45f
        val cornerRadius = 4.dp.toPx()

        // 가이드라인 (점선)
        val guidelineCount = 3
        for (i in 0..guidelineCount) {
            val y = topPadding + chartHeight * (1f - i.toFloat() / guidelineCount)
            drawLine(
                color = guidelineColor.copy(alpha = 0.4f),
                start = Offset(0f, y),
                end = Offset(size.width, y),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 4f))
            )
        }

        // 막대 + 라벨
        records.forEachIndexed { index, record ->
            val centerX = totalBarWidth * index + totalBarWidth / 2
            val ratio = if (maxValue > 0) record.solvedCount.toFloat() / maxValue else 0f
            val barHeight = chartHeight * ratio

            // 막대
            if (barHeight > 0) {
                drawRoundRect(
                    color = barColor,
                    topLeft = Offset(centerX - barWidth / 2, topPadding + chartHeight - barHeight),
                    size = Size(barWidth, barHeight),
                    cornerRadius = CornerRadius(cornerRadius, cornerRadius)
                )

                // 값 표시 (막대 위)
                val valueText = textMeasurer.measure("${record.solvedCount}", valueStyle)
                drawText(
                    textLayoutResult = valueText,
                    topLeft = Offset(
                        centerX - valueText.size.width / 2,
                        topPadding + chartHeight - barHeight - valueText.size.height - 2.dp.toPx()
                    )
                )
            }

            // 요일 라벨
            val isToday = index == records.lastIndex
            val style = if (isToday) todayLabelStyle else labelStyle
            val label = if (isToday) "오늘" else dayLabels[index]
            val labelResult = textMeasurer.measure(label, style)
            drawText(
                textLayoutResult = labelResult,
                topLeft = Offset(
                    centerX - labelResult.size.width / 2,
                    size.height - bottomPadding + 6.dp.toPx()
                )
            )
        }
    }
}
