package com.kero.idiom.feature.result.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kero.idiom.core.components.IdiomBaseCard
import com.kero.idiom.core.components.IdiomPrimaryButton
import com.kero.idiom.feature.result.contract.ResultIntent
import com.kero.idiom.feature.result.contract.ResultSideEffect
import com.kero.idiom.feature.result.viewmodel.ResultViewModel

@Composable
fun ResultScreen(
    score: Int,
    viewModel: ResultViewModel = hiltViewModel(),
    onNavigateToQuiz: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(score) {
        viewModel.processIntent(ResultIntent.Init(score))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ResultSideEffect.NavigateToQuiz -> onNavigateToQuiz()
                is ResultSideEffect.NavigateToHome -> onNavigateToHome()
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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.align(Alignment.Center)
            ) {
                Text(
                    text = "QUIZ COMPLETED",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    letterSpacing = 2.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                IdiomBaseCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Your Score",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                            )
                            Text(
                                text = "${state.score}",
                                style = MaterialTheme.typography.displayLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(48.dp))

                IdiomPrimaryButton(
                    text = "RETRY",
                    onClick = { viewModel.processIntent(ResultIntent.OnRetryClick) }
                )

                Spacer(modifier = Modifier.height(12.dp))

                IdiomPrimaryButton(
                    text = "GO HOME",
                    onClick = { viewModel.processIntent(ResultIntent.OnHomeClick) }
                )
            }
        }
    }
}
