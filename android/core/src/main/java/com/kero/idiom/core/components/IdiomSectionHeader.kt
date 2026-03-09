package com.kero.idiom.core.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kero.idiom.core.theme.BgPrimary
import com.kero.idiom.core.theme.IdiomQuizTheme
import com.kero.idiom.core.theme.TextPrimary
import com.kero.idiom.core.theme.TextSecondary

@Composable
fun IdiomSectionHeader(
    label: String,
    actionText: String = "",
    onActionClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.5.sp,
            color = TextSecondary
        )
        if (actionText.isNotEmpty()) {
            Text(
                text = actionText,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimary,
                modifier = Modifier.clickable(onClick = onActionClick)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun IdiomSectionHeaderPreview() {
    IdiomQuizTheme {
        Surface(color = BgPrimary) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                IdiomSectionHeader(
                    label = "최근 학습",
                    actionText = "전체보기",
                    onActionClick = {}
                )
                IdiomSectionHeader(
                    label = "오늘의 추천",
                    actionText = ""
                )
            }
        }
    }
}
