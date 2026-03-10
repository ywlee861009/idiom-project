package com.kero.idiom.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.kero.idiom.core.theme.BgSurface

/**
 * 한지(Hanji) 질감과 수묵화의 은은함을 표현하는 배경 컴포넌트.
 * 단순한 단색 배경보다 깊이감을 줍니다.
 */
@Composable
fun HanjiBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFCFCFC), // 아주 밝은 상단 (빛)
                        BgSurface,        // 기본 Soft White
                        Color(0xFFF0F0F0)  // 약간 더 차분한 하단 (무게감)
                    )
                )
            )
    ) {
        // 나중에 실제 이미지 리소스나 더 복잡한 패턴을 추가할 수도 있습니다.
        content()
    }
}
