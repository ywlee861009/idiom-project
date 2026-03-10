package com.kero.idiom.core.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kero.idiom.core.theme.BgDark
import com.kero.idiom.core.theme.BgPrimary
import com.kero.idiom.core.theme.BorderColor
import com.kero.idiom.core.theme.IdiomQuizTheme

@Composable
fun IdiomProgressBar(
    progress: Float,
    modifier: Modifier = Modifier
) {
    LinearProgressIndicator(
        progress = { progress },
        modifier = modifier
            .fillMaxWidth()
            .height(3.dp),
        color = BgDark,
        trackColor = BorderColor
    )
}

@Preview
@Composable
private fun IdiomProgressBarPreview() {
    IdiomQuizTheme {
        Surface(color = BgPrimary) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
            ) {
                Text("0%")
                IdiomProgressBar(progress = 0f)
                Text("40%")
                IdiomProgressBar(progress = 0.4f)
                Text("100%")
                IdiomProgressBar(progress = 1f)
            }
        }
    }
}
