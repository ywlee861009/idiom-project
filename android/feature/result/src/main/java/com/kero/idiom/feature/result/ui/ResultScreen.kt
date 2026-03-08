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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kero.idiom.core.components.HanjiBackground
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
        HanjiBackground(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp, vertical = 48.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "QUIZ COMPLETED",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary,
                        letterSpacing = 4.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(48.dp))

                    IdiomBaseCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Final Score",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                    letterSpacing = 2.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "${state.score}",
                                    style = MaterialTheme.typography.displayLarge.copy(fontSize = 72.sp),
                                    fontWeight = FontWeight.Black,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(64.dp))

                    IdiomPrimaryButton(
                        text = "TRY AGAIN",
                        onClick = { viewModel.processIntent(ResultIntent.OnRetryClick) }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    IdiomPrimaryButton(
                        text = "BACK TO HOME",
                        onClick = { viewModel.processIntent(ResultIntent.OnHomeClick) }
                    )
                }
            }
        }
    }
}
