package com.kero.idiom.core.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val SansSerifFamily = FontFamily.SansSerif
val SerifFamily = FontFamily.Serif

val Typography = Typography(
    // 페이지 타이틀 (서당, 서고, 내 정보) — 28sp Bold
    headlineLarge = TextStyle(
        fontFamily = SansSerifFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 34.sp,
        letterSpacing = (-1).sp
    ),
    // 퀴즈 질문 텍스트 — 20sp Bold
    headlineMedium = TextStyle(
        fontFamily = SansSerifFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 30.sp,
        letterSpacing = 0.sp
    ),
    // 랭크 이름 (초립동이) — 32sp Bold
    headlineSmall = TextStyle(
        fontFamily = SansSerifFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 38.sp,
        letterSpacing = (-1).sp
    ),
    // 한자 표시 (刻舟求劍) — 26sp Bold
    titleLarge = TextStyle(
        fontFamily = SansSerifFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp,
        lineHeight = 32.sp,
        letterSpacing = 4.sp
    ),
    // 섹션 헤더, 레이블 — 14sp SemiBold
    titleMedium = TextStyle(
        fontFamily = SansSerifFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp
    ),
    // 본문 텍스트 — 14sp Regular
    bodyLarge = TextStyle(
        fontFamily = SansSerifFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 21.sp,
        letterSpacing = 0.sp
    ),
    // 부제목, 설명 — 13sp Regular
    bodyMedium = TextStyle(
        fontFamily = SansSerifFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 19.sp,
        letterSpacing = 0.sp
    ),
    // 캡션, 날짜, 작은 텍스트 — 12sp Regular
    bodySmall = TextStyle(
        fontFamily = SansSerifFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 17.sp,
        letterSpacing = 0.5.sp
    ),
    // 버튼 텍스트 — 17sp Medium
    labelLarge = TextStyle(
        fontFamily = SansSerifFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 17.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.sp
    ),
    // 통계 수치 (87%) — 36sp
    displaySmall = TextStyle(
        fontFamily = SansSerifFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 42.sp,
        letterSpacing = (-2).sp
    ),
    // 탭 레이블 — 10sp SemiBold
    labelSmall = TextStyle(
        fontFamily = SansSerifFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 10.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.5.sp
    )
)
