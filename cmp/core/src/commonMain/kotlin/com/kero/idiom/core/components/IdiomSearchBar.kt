package com.kero.idiom.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kero.idiom.core.theme.BgPrimary
import com.kero.idiom.core.theme.BgSurface
import com.kero.idiom.core.theme.IdiomQuizTheme
import com.kero.idiom.core.theme.TextSecondary

@Composable
fun IdiomSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String = "검색",
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(BgSurface)
            .padding(horizontal = 16.dp),
        decorationBox = { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = null,
                    tint = TextSecondary
                )
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                if (query.isEmpty()) {
                    Text(text = placeholder, fontSize = 14.sp, color = TextSecondary)
                }
                innerTextField()
            }
        }
    )
}

@Preview
@Composable
private fun IdiomSearchBarPreview() {
    IdiomQuizTheme {
        Surface(color = BgPrimary) {
            Column(modifier = Modifier.padding(16.dp)) {
                var query by remember { mutableStateOf("") }
                IdiomSearchBar(
                    query = query,
                    onQueryChange = { query = it },
                    placeholder = "한자 검색"
                )
            }
        }
    }
}

@Preview
@Composable
private fun IdiomSearchBarFilledPreview() {
    IdiomQuizTheme {
        Surface(color = BgPrimary) {
            Column(modifier = Modifier.padding(16.dp)) {
                IdiomSearchBar(
                    query = "일석이조",
                    onQueryChange = {},
                    placeholder = "한자 검색"
                )
            }
        }
    }
}
