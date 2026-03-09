package com.kero.idiom.feature.quiz.ui

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kero.idiom.core.components.HanjiBackground
import com.kero.idiom.core.components.IdiomBaseCard
import com.kero.idiom.core.components.IdiomPrimaryButton
import com.kero.idiom.core.components.InkBurstEffect
import com.kero.idiom.domain.model.QuizType
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
    var showInkEffect by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is QuizSideEffect.ShowToast -> Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                is QuizSideEffect.NavigateToResult -> onNavigateToResult(effect.finalScore)
            }
        }
    }

    LaunchedEffect(state.isAnswerCorrect) {
        if (state.isAnswerCorrect == true) {
            showInkEffect = true
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
                    .padding(horizontal = 24.dp, vertical = 32.dp)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else {
                    AnimatedContent(
                        targetState = state.currentQuizIndex,
                        transitionSpec = {
                            (slideInHorizontally { width -> width / 2 } + fadeIn(animationSpec = tween(500)))
                                .togetherWith(slideOutHorizontally { width -> -width / 2 } + fadeOut(animationSpec = tween(500)))
                        },
                        label = "QuizTransition"
                    ) { targetIndex ->
                        state.quiz?.let { quiz ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                                    .padding(bottom = 40.dp)
                            ) {
                                // Header info
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = when(quiz.type) {
                                            QuizType.FILL_BLANK -> "빈칸에 들어갈 글자는?"
                                            QuizType.MEANING_TO_WORD -> "뜻에 알맞은 사자성어는?"
                                            QuizType.HANJA_TO_HANGUL -> "한자를 읽어보세요."
                                        },
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.secondary
                                    )

                                    Text(
                                        text = "$targetIndex / ${state.maxQuizzes}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.secondary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                // Quiz Card
                                IdiomBaseCard(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(min = 200.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                        Text(
                                            text = quiz.questionText,
                                            style = if (quiz.type == QuizType.MEANING_TO_WORD) 
                                                MaterialTheme.typography.displayMedium 
                                                else MaterialTheme.typography.displayLarge,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(16.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                // Hint Text (Meaning)
                                Text(
                                    text = quiz.hintText,
                                    style = MaterialTheme.typography.titleMedium,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )

                                Spacer(modifier = Modifier.height(32.dp))

                                // Options (Grid manually implemented for verticalScroll compatibility)
                                quiz.options.chunked(2).forEach { rowOptions ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        rowOptions.forEach { option ->
                                            Box(modifier = Modifier.weight(1f)) {
                                                IdiomPrimaryButton(
                                                    text = option,
                                                    onClick = { viewModel.processIntent(QuizIntent.SubmitAnswer(option)) },
                                                    enabled = state.isAnswerCorrect == null
                                                )
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                }
                                
                                Spacer(modifier = Modifier.height(16.dp))

                                // Current Score with Ticker Animation
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "SCORE: ",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = 3.sp,
                                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                                    )
                                    AnimatedContent(
                                        targetState = state.score,
                                        transitionSpec = {
                                            if (targetState > initialState) {
                                                (slideInVertically { height -> height } + fadeIn())
                                                    .togetherWith(slideOutVertically { height -> -height } + fadeOut())
                                            } else {
                                                (slideInVertically { height -> -height } + fadeIn())
                                                    .togetherWith(slideOutVertically { height -> height } + fadeOut())
                                            }.using(SizeTransform(clip = false))
                                        },
                                        label = "ScoreTicker"
                                    ) { targetScore ->
                                        Text(
                                            text = "$targetScore",
                                            style = MaterialTheme.typography.labelLarge,
                                            fontWeight = FontWeight.ExtraBold,
                                            letterSpacing = 3.sp,
                                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                
                if (showInkEffect) {
                    InkBurstEffect(onAnimationEnd = { showInkEffect = false })
                }
            }
        }
    }
}
