package com.kero.idiom.ui.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kero.idiom.core.theme.*
import com.kero.idiom.core.util.DateUtils
import com.kero.idiom.domain.model.DailyRecord

@Composable
fun MonthlyStreakCalendar(
    yearMonth: String,
    monthlyRecords: List<DailyRecord>,
    activeDaysCount: Int,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    val recordMap = monthlyRecords.associateBy { DateUtils.getDayOfMonth(it.date) }
    val daysInMonth = DateUtils.getDaysInMonth(yearMonth)
    val firstDayOfWeek = DateUtils.getFirstDayOfWeekInMonth(yearMonth) // 월=0 ~ 일=6
    val todayString = DateUtils.getTodayDateString()
    val currentYearMonth = DateUtils.getYearMonthString()
    val todayDay = if (yearMonth == currentYearMonth) DateUtils.getDayOfMonth(todayString) else -1

    val parts = yearMonth.split("-")
    val displayTitle = "${parts[0]}년 ${parts[1].toInt()}월"
    val isCurrentMonth = yearMonth == currentYearMonth

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(BgSurface)
            .border(1.dp, BorderColor, RoundedCornerShape(16.dp))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header: 제목 + 월 이동 버튼
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "학습 캘린더",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                Text(
                    text = "${activeDaysCount}일 학습",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "<",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { onPreviousMonth() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
                Text(
                    text = displayTitle,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = ">",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isCurrentMonth) BorderColor else TextPrimary,
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable(enabled = !isCurrentMonth) { onNextMonth() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        // 요일 헤더
        val dayLabels = listOf("일", "월", "화", "수", "목", "금", "토")
        Row(modifier = Modifier.fillMaxWidth()) {
            dayLabels.forEach { label ->
                Text(
                    text = label,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
            }
        }

        // 날짜 그리드
        val totalCells = firstDayOfWeek + daysInMonth
        val rows = (totalCells + 6) / 7

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            for (row in 0 until rows) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    for (col in 0..6) {
                        val cellIndex = row * 7 + col
                        val day = cellIndex - firstDayOfWeek + 1

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            if (day in 1..daysInMonth) {
                                val record = recordMap[day]
                                val solvedCount = record?.solvedCount ?: 0
                                val isToday = day == todayDay
                                val bgColor = getIntensityColor(solvedCount)

                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(bgColor)
                                        .then(
                                            if (isToday) Modifier.border(
                                                2.dp,
                                                BgDark,
                                                RoundedCornerShape(6.dp)
                                            ) else Modifier
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "$day",
                                        fontSize = 12.sp,
                                        color = if (solvedCount >= 10) Color.White else TextPrimary,
                                        fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 범례
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "적음",
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                modifier = Modifier.padding(end = 4.dp)
            )
            listOf(0, 2, 5, 10).forEach { level ->
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(getIntensityColor(level))
                        .border(0.5.dp, BorderColor.copy(alpha = 0.5f), RoundedCornerShape(2.dp))
                )
                Spacer(Modifier.width(2.dp))
            }
            Text(
                text = "많음",
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                modifier = Modifier.padding(start = 2.dp)
            )
        }
    }
}

private fun getIntensityColor(solvedCount: Int): Color {
    return when {
        solvedCount == 0 -> Color(0xFFEBEDF0) // 빈 칸
        solvedCount in 1..4 -> Color(0xFFD4D4D4) // 연한 회색
        solvedCount in 5..9 -> Color(0xFF8C8C8C) // 중간 회색
        else -> Color(0xFF2C2C2C) // 진한 먹색
    }
}
