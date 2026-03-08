package com.kero.idiom.core.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun IdiomBaseCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp), // 조금 더 단단한 느낌
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background // Soft White
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary), // Deep Navy Border
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        content()
    }
}
