package com.kero.idiom.core.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kero.idiom.core.theme.BgPrimary
import com.kero.idiom.core.theme.BorderColor
import com.kero.idiom.core.theme.IdiomQuizTheme
import com.kero.idiom.core.theme.TextSecondary

@Composable
fun IdiomListItem(
    hanjaText: String,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f).padding(vertical = 12.dp)) {
                Text(text = hanjaText, fontSize = 18.sp)
                Text(text = description, fontSize = 12.sp, color = TextSecondary)
            }
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = TextSecondary
            )
        }
        HorizontalDivider(color = BorderColor, thickness = 1.dp)
    }
}

@Preview(showBackground = true)
@Composable
private fun IdiomListItemPreview() {
    IdiomQuizTheme {
        Surface(color = BgPrimary) {
            Column {
                IdiomListItem(
                    hanjaText = "一石二鳥",
                    description = "한 번의 행동으로 두 가지 이득을 얻음",
                    onClick = {}
                )
                IdiomListItem(
                    hanjaText = "大器晩成",
                    description = "큰 그릇은 늦게 이루어진다",
                    onClick = {}
                )
                IdiomListItem(
                    hanjaText = "七顚八起",
                    description = "일곱 번 넘어져도 여덟 번 일어난다",
                    onClick = {}
                )
            }
        }
    }
}
