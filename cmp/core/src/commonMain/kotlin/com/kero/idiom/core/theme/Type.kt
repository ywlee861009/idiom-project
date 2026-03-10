package com.kero.idiom.core.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Senior-Friendly Silver UI Typography
// Serif(명조체)는 어르신들께 친숙하고 가독성이 좋습니다.
val SerifFamily = FontFamily.Serif 
val SansSerifFamily = FontFamily.SansSerif

val Typography = Typography(
    // Main Quiz Word
    displayLarge = TextStyle(
        fontFamily = SerifFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 56.sp,
        lineHeight = 64.sp,
        letterSpacing = 0.sp
    ),
    // Middle Size Text
    displayMedium = TextStyle(
        fontFamily = SerifFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 44.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp
    ),
    // Score Text
    displaySmall = TextStyle(
        fontFamily = SansSerifFamily,
        fontWeight = FontWeight.Black,
        fontSize = 64.sp,
        letterSpacing = 1.sp
    ),
    // Meaning/Hint Text (Most important for seniors)
    titleMedium = TextStyle(
        fontFamily = SerifFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp,
        lineHeight = 34.sp,
        letterSpacing = 0.sp
    ),
    // Button Text
    labelLarge = TextStyle(
        fontFamily = SansSerifFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.5.sp
    ),
    // Info/Caption (e.g., Progress info)
    labelSmall = TextStyle(
        fontFamily = SansSerifFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.5.sp
    )
)
