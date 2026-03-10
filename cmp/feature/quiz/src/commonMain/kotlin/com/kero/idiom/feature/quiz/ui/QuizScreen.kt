package com.kero.idiom.feature.quiz.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kero.idiom.core.components.HanjiBackground
import com.kero.idiom.core.components.IdiomBaseCard
import com.kero.idiom.domain.model.QuizType
import com.kero.idiom.feature.quiz.contract.QuizIntent
import com.kero.idiom.feature.quiz.contract.QuizSideEffect
import com.kero.idiom.feature.quiz.viewmodel.QuizViewModel
import io.github.alexzhirkevich.compottie.*
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.compose.viewmodel.koinViewModel
import idiom_cmp.feature.quiz.generated.resources.Res

@OptIn(ExperimentalResourceApi::class)
@Composable
fun QuizScreen(
    viewModel: QuizViewModel = koinViewModel(),
    onNavigateToResult: (Int, Int) -> Unit
) {
    val state by viewModel.state.collectAsState()
    var currentLottieFile by remember { mutableStateOf<String?>(null) }
    
    // CMP 리소스 바이트 상태 관리
    var compositionBytes by remember { mutableStateOf<ByteArray?>(null) }
    
    // 리소스를 바이트로 읽어옴
    LaunchedEffect(currentLottieFile) {
        val file = currentLottieFile
        if (file != null) {
            try {
                compositionBytes = Res.readBytes("files/$file")
            } catch (e: Exception) {
                compositionBytes = null
            }
        } else {
            compositionBytes = null
        }
    }

    // 바이트 기반으로 LottieComposition 생성
    val composition by rememberLottieComposition {
        val bytes = compositionBytes
        if (bytes != null) {
            if (currentLottieFile?.endsWith(".lottie") == true) {
                LottieCompositionSpec.DotLottie(bytes)
            } else {
                LottieCompositionSpec.JsonString(bytes.decodeToString())
            }
        } else {
            LottieCompositionSpec.JsonString("{}")
        }
    }
    
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = currentLottieFile != null,
        iterations = 1
    )

    // 애니메이션 종료 후 다음 퀴즈로 이동
    LaunchedEffect(progress) {
        if (progress == 1f && currentLottieFile != null) {
            currentLottieFile = null
            viewModel.handleIntent(QuizIntent.NextQuiz)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is QuizSideEffect.NavigateToResult -> onNavigateToResult(effect.score, effect.total)
                QuizSideEffect.ShowCorrectEffect -> { 
                    currentLottieFile = "success.lottie"
                }
                QuizSideEffect.ShowWrongEffect -> { 
                    currentLottieFile = "wrong.lottie"
                }
            }
        }
    }

    Scaffold(
        containerColor = Color(0xFFF9F7F2)
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
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color(0xFF2C2C2C))
                } else {
                    state.currentQuiz?.let { quiz ->
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
                                    color = Color(0xFF2C2C2C).copy(alpha = 0.6f)
                                )

                                Text(
                                    text = "${state.quizCount + 1} / 10",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF2C2C2C).copy(alpha = 0.6f),
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
                                        modifier = Modifier.padding(16.dp),
                                        color = Color(0xFF2C2C2C)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // Hint Text (Meaning)
                            Text(
                                text = quiz.hintText,
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.Center,
                                color = Color(0xFF2C2C2C).copy(alpha = 0.7f),
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )

                            Spacer(modifier = Modifier.height(32.dp))

                            // Options
                            quiz.options.forEachIndexed { index, option ->
                                if (index % 2 == 0) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        QuizOptionButton(
                                            modifier = Modifier.weight(1f),
                                            text = option,
                                            isSelected = state.selectedOption == option,
                                            isCorrect = option == quiz.answer,
                                            showResult = state.selectedOption != null,
                                            onClick = { viewModel.handleIntent(QuizIntent.SelectOption(option)) }
                                        )
                                        if (index + 1 < quiz.options.size) {
                                            val nextOption = quiz.options[index + 1]
                                            QuizOptionButton(
                                                modifier = Modifier.weight(1f),
                                                text = nextOption,
                                                isSelected = state.selectedOption == nextOption,
                                                isCorrect = nextOption == quiz.answer,
                                                showResult = state.selectedOption != null,
                                                onClick = { viewModel.handleIntent(QuizIntent.SelectOption(nextOption)) }
                                            )
                                        } else {
                                            Spacer(modifier = Modifier.weight(1f))
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))

                            // Score Ticker
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "SCORE: ",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF2C2C2C).copy(alpha = 0.8f)
                                )
                                Text(
                                    text = "${state.score}",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF2C2C2C).copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                }
                
                // Lottie Animation Overlay
                if (currentLottieFile != null && composition != null) {
                    val lottiePainter = rememberLottiePainter(
                        composition = composition,
                        progress = { progress } // 람다로 전달하여 타입 에러 해결
                    )
                    
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.1f))
                            .clickable(enabled = false) {},
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = lottiePainter,
                            contentDescription = "Lottie Animation",
                            modifier = Modifier.size(350.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuizOptionButton(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    showResult: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        !showResult -> Color(0xFF2C2C2C)
        isCorrect -> Color(0xFF4CAF50)
        else -> Color(0xFFE57373)
    }

    val border = if (isSelected) {
        BorderStroke(10.dp, Color(0xFF2196F3))
    } else {
        BorderStroke(1.dp, Color(0xFF2C2C2C).copy(alpha = 0.1f))
    }

    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp),
        enabled = !showResult,
        shape = RoundedCornerShape(12.dp),
        border = border,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = Color.White,
            disabledContainerColor = backgroundColor,
            disabledContentColor = Color.White
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}
