package com.kero.idiom.feature.quiz.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kero.idiom.core.components.IdiomBaseCard
import com.kero.idiom.core.components.IdiomPrimaryButton
import com.kero.idiom.feature.quiz.contract.QuizIntent
import com.kero.idiom.feature.quiz.contract.QuizSideEffect
import com.kero.idiom.feature.quiz.viewmodel.QuizViewModel

@Composable
fun QuizScreen(
    viewModel: QuizViewModel = hiltViewModel(),
    onNavigateToResult: (Int) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is QuizSideEffect.ShowToast -> Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                is QuizSideEffect.NavigateToResult -> onNavigateToResult(effect.finalScore)
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                state.quiz?.let { quiz ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(32.dp),
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        // Score Header
                        Text(
                            text = "Score: ${state.score}",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Quiz Content
                        IdiomBaseCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = quiz.questionWord,
                                    style = MaterialTheme.typography.displayLarge,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        // Meaning Hint
                        Text(
                            text = quiz.originalIdiom.meaning,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        // Options Grid
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            contentPadding = PaddingValues(bottom = 16.dp)
                        ) {
                            items(quiz.options) { char ->
                                IdiomPrimaryButton(
                                    text = char.toString(),
                                    onClick = { viewModel.processIntent(QuizIntent.SubmitAnswer(char)) },
                                    enabled = state.isAnswerCorrect == null // 정답 처리 중엔 버튼 비활성화
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
