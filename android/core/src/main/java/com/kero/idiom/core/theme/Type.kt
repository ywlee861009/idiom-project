package com.kero.idiom.core.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Typography optimized for 'Extreme Minimalism'
val Typography = Typography(
    // 퀴즈 문제 (압도적인 존재감)
    displayLarge = TextStyle(
        fontFamily = FontFamily.Serif, // 명조 계열로 진중함 표현
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp,
        lineHeight = 56.sp,
        letterSpacing = 4.sp,
        color = DeepNavy
    ),
    // 메인 타이틀
    titleLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        color = DeepNavy
    ),
    // 버튼 및 강조 텍스트
    labelLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        color = SoftWhite // 버튼 내부 텍스트
    ),
    // 일반 본문
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        color = DeepNavy
    )
)
