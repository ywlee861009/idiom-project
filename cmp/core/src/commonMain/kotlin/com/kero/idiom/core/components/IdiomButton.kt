package com.kero.idiom.core.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kero.idiom.core.theme.BgDark
import com.kero.idiom.core.theme.BgPrimary
import com.kero.idiom.core.theme.BorderColor
import com.kero.idiom.core.theme.TextOnDark
import com.kero.idiom.core.theme.TextPrimary
import com.kero.idiom.core.theme.IdiomQuizTheme

@Composable
fun IdiomPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = BgDark,
            contentColor = TextOnDark
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 0.dp)
        )
    }
}

@Composable
fun IdiomSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, BorderColor),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = BgPrimary,
            contentColor = TextPrimary
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 0.dp)
        )
    }
}

@Preview
@Composable
private fun IdiomButtonPreview() {
    IdiomQuizTheme {
        Surface(color = BgPrimary) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IdiomPrimaryButton(text = "다음 문제", onClick = {})
                IdiomSecondaryButton(text = "건너뛰기", onClick = {})
                IdiomPrimaryButton(text = "비활성화", onClick = {}, enabled = false)
            }
        }
    }
}
