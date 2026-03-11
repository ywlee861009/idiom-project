package com.kero.idiom.feature.quiz.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kero.idiom.core.theme.*
import com.kero.idiom.domain.model.QuizType
import com.kero.idiom.feature.quiz.contract.QuizIntent
import com.kero.idiom.feature.quiz.contract.QuizSideEffect
import com.kero.idiom.feature.quiz.viewmodel.QuizViewModel
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import io.github.alexzhirkevich.compottie.DotLottie
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.compose.viewmodel.koinViewModel
import idiom_cmp.feature.quiz.generated.resources.Res

private const val TOTAL = 5

@OptIn(ExperimentalResourceApi::class)
@Composable
fun QuizScreen(
    viewModel: QuizViewModel = koinViewModel(),
    onNavigateToResult: (Int, Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    var animationFile by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    // animationFile을 key로 전달하여 상태가 바뀔 때마다 다시 로드하도록 강제함
    val compositionResult = rememberLottieComposition(animationFile) {
        val path = when (animationFile) {
            "success" -> "files/success.lottie"
            "wrong" -> "files/wrong.lottie"
            else -> null
        }
        
        if (path != null) {
            // .lottie(ZIP) 파일이므로 DotLottie 사용
            LottieCompositionSpec.DotLottie(Res.readBytes(path))
        } else {
            // null 대신 빈 JSON으로 초기화 에러 방지
            LottieCompositionSpec.JsonString("{}")
        }
    }
    
    val composition by compositionResult

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is QuizSideEffect.NavigateToResult ->
                    onNavigateToResult(effect.score, effect.total)
                QuizSideEffect.ShowCorrectEffect -> scope.launch {
                    animationFile = "success"
                    delay(500)
                    animationFile = null
                }
                QuizSideEffect.ShowWrongEffect -> scope.launch {
                    animationFile = "wrong"
                    delay(500)
                    animationFile = null
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BgPrimary)
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            if (state.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = BgDark)
                }
            } else {
                state.currentQuiz?.let { quiz ->
                    // 상단 바
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .clickable { onNavigateBack() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("✕", fontSize = 16.sp, color = TextPrimary)
                        }
                        Text(
                            text = "${state.quizCount} / $TOTAL",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            color = TextMuted
                        )
                    }

                    // 진행 바
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(BorderColor)
                    ) {
                        val progress = state.quizCount.toFloat() / TOTAL
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(progress)
                                .clip(RoundedCornerShape(2.dp))
                                .background(BgDark)
                        )
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 24.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Spacer(Modifier.height(24.dp))

                        // 질문 박스
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(BgSurface)
                                .padding(horizontal = 24.dp, vertical = 28.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = when (quiz.type) {
                                    QuizType.FILL_BLANK -> "빈칸에 들어갈 글자는?"
                                    QuizType.MEANING_TO_WORD -> "뜻을 보고 맞히기"
                                    QuizType.HANJA_TO_HANGUL -> "한자를 읽어보세요"
                                    QuizType.FILL_BLANKS_2 -> "빈칸 두 개를 채워보세요"
                                    QuizType.FILL_BLANKS_4 -> "사자성어를 완성해 보세요"
                                },
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextMuted,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = if (state.isCorrect == true) quiz.answer else quiz.questionText,
                                style = MaterialTheme.typography.headlineMedium,
                                color = TextPrimary,
                                modifier = Modifier.fillMaxWidth(),
                                letterSpacing = 4.sp
                            )
                            if (quiz.hintText.isNotEmpty()) {
                                Text(
                                    text = quiz.hintText,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextMuted
                                )
                            }
                        }

                        Spacer(Modifier.height(24.dp))

                        // 선택지 또는 입력 필드
                        if (quiz.options.isNotEmpty()) {
                            quiz.options.forEachIndexed { index, option ->
                                val isSelected = state.selectedOption == option
                                val isCorrect = option == quiz.answer
                                val showResult = state.selectedOption != null

                                val bgColor = when {
                                    !showResult -> BgSurface
                                    isCorrect -> CorrectBg
                                    isSelected -> WrongBg
                                    else -> BgSurface
                                }
                                val borderColor = when {
                                    !showResult -> BorderColor
                                    isCorrect -> CorrectGreen
                                    isSelected -> WrongRed
                                    else -> BorderColor
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 12.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(bgColor)
                                        .border(1.dp, borderColor, RoundedCornerShape(14.dp))
                                        .clickable(enabled = !showResult) {
                                            viewModel.handleIntent(QuizIntent.SelectOption(option))
                                        }
                                        .padding(horizontal = 20.dp, vertical = 18.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Text(
                                            text = "${index + 1}",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = if (showResult && isCorrect) CorrectGreen
                                            else if (showResult && isSelected) WrongRed
                                            else TextMuted
                                        )
                                        Text(
                                            text = option,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = TextPrimary
                                        )
                                    }
                                    if (showResult && isCorrect) {
                                        Text("✓", color = CorrectGreen, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        } else {
                            // 주관식 입력 필드
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                OutlinedTextField(
                                    value = state.inputText,
                                    onValueChange = { 
                                        if (it.length <= 4 && state.isCorrect == null) {
                                            viewModel.handleIntent(QuizIntent.InputAnswer(it))
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    textStyle = MaterialTheme.typography.headlineSmall.copy(
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                        letterSpacing = 8.sp
                                    ),
                                    placeholder = {
                                        Text(
                                            "${quiz.blankIndices.size}글자 또는 4글자 입력",
                                            modifier = Modifier.fillMaxWidth(),
                                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                            fontSize = 18.sp,
                                            color = TextMuted
                                        )
                                    },
                                    singleLine = true,
                                    shape = RoundedCornerShape(14.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = BgDark,
                                        unfocusedBorderColor = BorderColor,
                                        focusedContainerColor = BgSurface,
                                        unfocusedContainerColor = BgSurface,
                                        disabledContainerColor = if (state.isCorrect == true) CorrectBg else WrongBg,
                                        disabledBorderColor = if (state.isCorrect == true) CorrectGreen else WrongRed
                                    ),
                                    enabled = state.isCorrect == null
                                )

                                if (state.isCorrect == null) {
                                    Button(
                                        onClick = { viewModel.handleIntent(QuizIntent.SubmitAnswer) },
                                        modifier = Modifier.fillMaxWidth().height(56.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = BgDark),
                                        enabled = state.inputText.isNotBlank()
                                    ) {
                                        Text("정답 확인", fontWeight = FontWeight.Bold)
                                    }
                                } else if (state.isCorrect == false) {
                                    Text(
                                        text = "정답은 '${quiz.answer}' 입니다.",
                                        color = WrongRed,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 16.sp
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(16.dp))
                    }

                    // 다음 문제 버튼
                    val isLastQuestion = state.quizCount >= TOTAL
                    val showResult = state.selectedOption != null || state.isCorrect != null
                    val buttonEnabled = showResult && animationFile == null

                    Button(
                        onClick = { viewModel.handleIntent(QuizIntent.NextQuiz) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .padding(bottom = 40.dp)
                            .height(60.dp),
                        enabled = buttonEnabled,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BgDark,
                            contentColor = TextOnDark,
                            disabledContainerColor = BorderColor,
                            disabledContentColor = TextMuted
                        )
                    ) {
                        Text(
                            text = if (isLastQuestion) "결과 보기 →" else "다음 문제 →",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Lottie Overlay (animationFile이 있을 때만 렌더링)
        if (animationFile != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(enabled = false) { },
                contentAlignment = Alignment.Center
            ) {
                val comp = composition
                if (comp != null) {
                    val durationSec = comp.durationFrames / comp.frameRate
                    val speed = durationSec / 0.5f
                    val progress by animateLottieCompositionAsState(
                        composition = comp,
                        isPlaying = animationFile != null,
                        iterations = 1,
                        speed = speed
                    )
                    val painter = rememberLottiePainter(
                        composition = comp,
                        progress = { progress }
                    )
                    Image(
                        painter = painter,
                        contentDescription = "Lottie Animation",
                        modifier = Modifier.size(320.dp)
                    )
                } else {
                    CircularProgressIndicator(color = BgDark, modifier = Modifier.size(48.dp))
                }
            }
        }
    }
}
