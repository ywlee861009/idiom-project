package com.kero.idiom.core.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Pretendard 폰트 파일이 res/font/에 추가되면 아래 FontFamily.Default를 교체
private val PretendardFamily = FontFamily.Default

val Typography = Typography(
    // Display — 48sp Regular
    displayLarge = TextStyle(
        fontFamily = PretendardFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 48.sp,
        letterSpacing = 0.sp
    ),
    // Question — 42sp Regular
    displayMedium = TextStyle(
        fontFamily = PretendardFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 42.sp,
        letterSpacing = 0.sp
    ),
    // Score — 72sp Regular
    displaySmall = TextStyle(
        fontFamily = PretendardFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 72.sp,
        letterSpacing = 0.sp
    ),
    // Heading — 28sp Regular
    headlineMedium = TextStyle(
        fontFamily = PretendardFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        letterSpacing = 0.sp
    ),
    // Subtitle — 18sp Regular
    titleMedium = TextStyle(
        fontFamily = PretendardFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        letterSpacing = 0.sp
    ),
    // Button — 16sp Medium
    labelLarge = TextStyle(
        fontFamily = PretendardFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        letterSpacing = 0.sp
    ),
    // Caption — 12sp Medium, 0.5sp tracking
    labelSmall = TextStyle(
        fontFamily = PretendardFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        letterSpacing = 0.5.sp
    )
)
