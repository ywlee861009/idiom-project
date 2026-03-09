package com.kero.idiom.core.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kero.idiom.core.theme.BgPrimary
import com.kero.idiom.core.theme.BorderColor
import com.kero.idiom.core.theme.IdiomQuizTheme

@Composable
fun IdiomDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        modifier = modifier,
        thickness = 1.dp,
        color = BorderColor
    )
}

@Preview(showBackground = true)
@Composable
private fun IdiomDividerPreview() {
    IdiomQuizTheme {
        Surface(color = BgPrimary) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("위 항목", modifier = Modifier.padding(bottom = 12.dp))
                IdiomDivider()
                Text("아래 항목", modifier = Modifier.padding(top = 12.dp))
            }
        }
    }
}
