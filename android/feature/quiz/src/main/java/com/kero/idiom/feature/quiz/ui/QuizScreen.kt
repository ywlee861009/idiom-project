package com.kero.idiom.feature.quiz.ui

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    val state by viewModel.state.collectAsStateWithLifecycle()
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

    // 정답 시 먹물 효과 트리거
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
                    .padding(horizontal = 32.dp, vertical = 48.dp)
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
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxSize()
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
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.secondary,
                                        letterSpacing = 2.sp
                                    )

                                    Text(
                                        text = "$targetIndex / ${state.maxQuizzes}",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.secondary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                            Spacer(modifier = Modifier.height(40.dp))

                            // Quiz Card
                            IdiomBaseCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(280.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                    Text(
                                        text = quiz.questionText,
                                        style = if (quiz.type == QuizType.MEANING_TO_WORD) 
                                            MaterialTheme.typography.titleLarge 
                                            else MaterialTheme.typography.displayLarge,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(24.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(32.dp))

                            // Hint Text
                            Text(
                                text = quiz.hintText,
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                letterSpacing = 1.sp
                            )

                            Spacer(modifier = Modifier.height(48.dp))

                            // Options
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                contentPadding = PaddingValues(bottom = 24.dp)
                            ) {
                                items(quiz.options) { option ->
                                    IdiomPrimaryButton(
                                        text = option,
                                        onClick = { viewModel.processIntent(QuizIntent.SubmitAnswer(option)) },
                                        enabled = state.isAnswerCorrect == null
                                    )
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(24.dp))

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
                
                // Ink Burst Animation Overlay
                if (showInkEffect) {
                    InkBurstEffect(
                        onAnimationEnd = { showInkEffect = false }
                    )
                }
            }
        }
    }
}
