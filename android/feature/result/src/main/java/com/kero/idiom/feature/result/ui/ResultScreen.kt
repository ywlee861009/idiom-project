package com.kero.idiom.feature.result.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ResultScreen(score: Int) {
    Text(text = "최종 점수: $score")
}
