package com.kero.idiom.core.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kero.idiom.core.theme.BgPrimary
import com.kero.idiom.core.theme.BorderColor
import com.kero.idiom.core.theme.TextSecondary
import com.kero.idiom.core.theme.IdiomQuizTheme

@Composable
fun IdiomBaseCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        content()
    }
}

@Composable
fun IdiomOptionCard(
    hanja: String,
    meaning: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = BgPrimary),
        border = BorderStroke(1.dp, BorderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            Text(
                text = hanja,
                fontSize = 22.sp,
                fontStyle = FontStyle.Italic
            )
            Text(
                text = meaning,
                fontSize = 13.sp,
                color = TextSecondary
            )
        }
    }
}

@Composable
fun IdiomStatCard(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = BgPrimary),
        border = BorderStroke(1.dp, BorderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 24.dp)
        ) {
            Text(
                text = value,
                fontSize = 36.sp,
                fontStyle = FontStyle.Italic,
                letterSpacing = (-2).sp
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun IdiomBaseCardPreview() {
    IdiomQuizTheme {
        Surface(color = BgPrimary) {
            IdiomBaseCard(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "一石二鳥",
                    fontSize = 28.sp,
                    modifier = Modifier.padding(24.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun IdiomOptionCardPreview() {
    IdiomQuizTheme {
        Surface(color = BgPrimary) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IdiomOptionCard(hanja = "一石二鳥", meaning = "한 번의 행동으로 두 가지 이득을 얻음")
                IdiomOptionCard(hanja = "大器晩成", meaning = "큰 그릇은 늦게 이루어진다")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun IdiomStatCardPreview() {
    IdiomQuizTheme {
        Surface(color = BgPrimary) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IdiomStatCard(value = "8", label = "정답", modifier = Modifier.weight(1f))
                IdiomStatCard(value = "2", label = "오답", modifier = Modifier.weight(1f))
            }
        }
    }
}
