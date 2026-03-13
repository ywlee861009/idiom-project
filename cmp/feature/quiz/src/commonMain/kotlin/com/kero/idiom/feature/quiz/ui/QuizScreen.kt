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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kero.idiom.core.theme.*
import com.kero.idiom.domain.model.QuizType
import com.kero.idiom.feature.quiz.contract.QuizIntent
import com.kero.idiom.feature.quiz.contract.QuizSideEffect
import com.kero.idiom.feature.quiz.viewmodel.QuizViewModel
import io.github.alexzhirkevich.compottie.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.compose.viewmodel.koinViewModel
import idiom_cmp.feature.quiz.generated.resources.Res

private const val TOTAL = 5

@Composable
private fun HintButton(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(HintBg)
            .border(1.5.dp, HintOrange, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text("💡", fontSize = 18.sp)
        Spacer(Modifier.width(8.dp))
        Text(
            text = "힌트 보기",
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = HintDarkOrange
        )
        Spacer(Modifier.width(10.dp))
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(HintOrange)
                .padding(horizontal = 8.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "광고",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
            Text("▶", fontSize = 11.sp, color = Color.White)
        }
    }
}

@Composable
private fun HintRevealCard(word: String, blankIndices: List<Int>) {
    val firstBlankIndex = blankIndices.firstOrNull() ?: 0
    val revealedChar = word.getOrNull(firstBlankIndex)?.toString() ?: "?"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(HintBg)
            .border(1.5.dp, HintOrange, RoundedCornerShape(16.dp))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("💡", fontSize = 16.sp)
            Text(
                text = "힌트 — 빈칸 첫 번째 글자",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = HintDarkOrange
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            word.forEachIndexed { index, char ->
                val isBlank = index in blankIndices
                val isRevealed = index == firstBlankIndex
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isRevealed) BgDark else BgSurface)
                        .then(
                            if (!isRevealed) Modifier.border(1.5.dp, BorderColor, RoundedCornerShape(12.dp))
                            else Modifier
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isBlank && !isRevealed) "?" else char.toString(),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isRevealed) TextOnDark else if (isBlank) TextMuted else TextPrimary
                    )
                }
            }
        }
        Text(
            text = "광고를 시청하면 빈칸의 첫 번째 글자 '${revealedChar}'을 공개해 드렸어요.",
            fontSize = 12.sp,
            color = HintDarkOrange
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun QuizScreen(
    viewModel: QuizViewModel = koinViewModel(),
    onNavigateToResult: (Int, Int, Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var animationFile by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(state.currentQuiz) {
        if (state.currentQuiz != null && state.currentQuiz?.options?.isEmpty() == true) {
            delay(300)
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }

    val compositionResult = rememberLottieComposition(animationFile) {
        val path = when (animationFile) {
            "success" -> "files/success.lottie"
            "wrong" -> "files/wrong.lottie"
            else -> null
        }
        
        if (path != null) {
            LottieCompositionSpec.DotLottie(Res.readBytes(path))
        } else {
            LottieCompositionSpec.JsonString("{}")
        }
    }
    
    val composition by compositionResult

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is QuizSideEffect.NavigateToResult ->
                    onNavigateToResult(effect.score, effect.total, effect.xpGained)
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
                is QuizSideEffect.ShowToast -> scope.launch {
                    snackbarHostState.showSnackbar(effect.message)
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
                .imePadding()
        ) {
            if (state.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = BgDark)
                }
            } else {
                state.currentQuiz?.let { quiz ->
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

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(BgSurface)
                                .padding(horizontal = 24.dp, vertical = 28.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
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

                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(HintOrange.copy(alpha = 0.1f))
                                        .border(0.5.dp, HintOrange.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = "${quiz.originalIdiom.level} XP",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = HintDarkOrange
                                    )
                                }
                            }
                            val isSubjective = quiz.options.isEmpty()
                            val showFullAnswer = state.isCorrect != null
                            
                            Text(
                                text = when {
                                    showFullAnswer -> quiz.originalIdiom.word // 🌟 정답 확인 후에는 항상 전체 성어(한글) 표시
                                    isSubjective -> quiz.originalIdiom.hanja // 주관식 정답 전에는 한자 표시
                                    else -> quiz.questionText // 객관식은 질문 텍스트(예: 백전백_) 표시
                                },
                                style = MaterialTheme.typography.headlineMedium,
                                color = TextPrimary,
                                modifier = Modifier.fillMaxWidth(),
                                letterSpacing = 4.sp
                            )
                            if (quiz.hintText.isNotEmpty() && (quiz.options.isEmpty() || state.isHintRevealed)) {
                                Text(
                                    text = quiz.hintText,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextMuted
                                )
                            }
                        }

                        Spacer(Modifier.height(16.dp))

                        if (!state.isHintRevealed && state.selectedOption == null && state.isCorrect == null) {
                            HintButton(onClick = { viewModel.handleIntent(QuizIntent.ShowHint) })
                            Spacer(Modifier.height(12.dp))
                        }

                        if (state.isHintRevealed && quiz.options.isEmpty()) {
                            HintRevealCard(word = quiz.originalIdiom.word, blankIndices = quiz.blankIndices)
                            Spacer(Modifier.height(12.dp))
                        }

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
                            val idiomWord = quiz.originalIdiom.word
                            val blankIndices = quiz.blankIndices
                            
                            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                BasicTextField(
                                    value = state.inputText,
                                    onValueChange = { newValue ->
                                        if (state.isCorrect != null) return@BasicTextField
                                        if (newValue.length <= blankIndices.size) {
                                            viewModel.handleIntent(QuizIntent.InputAnswer(newValue))
                                        }
                                    },
                                    modifier = Modifier
                                        .size(1.dp)
                                        .focusRequester(focusRequester),
                                    keyboardOptions = KeyboardOptions(
                                        imeAction = ImeAction.Done
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onDone = {
                                            if (state.inputText.length == blankIndices.size) {
                                                keyboardController?.hide()
                                                viewModel.handleIntent(QuizIntent.SubmitAnswer)
                                            }
                                        }
                                    )
                                )

                                Row(
                                    modifier = Modifier.clickable { 
                                        focusRequester.requestFocus()
                                        keyboardController?.show() // 🌟 강제로 키보드 다시 띄우기
                                    },
                                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                                ) {
                                    idiomWord.forEachIndexed { index, char ->
                                        val isBlank = index in blankIndices
                                        
                                        if (isBlank) {
                                            val blankPos = blankIndices.indexOf(index)
                                            val charValue = state.inputText.getOrNull(blankPos)?.toString() ?: ""
                                            val isCurrent = blankPos == state.inputText.length && state.isCorrect == null
                                            
                                            val borderColor = when {
                                                state.isCorrect == true -> CorrectGreen
                                                state.isCorrect == false -> WrongRed
                                                isCurrent -> BgDark
                                                else -> BorderColor
                                            }
                                            
                                            val bgColor = when {
                                                state.isCorrect == true -> CorrectBg
                                                state.isCorrect == false -> WrongBg
                                                else -> BgSurface
                                            }

                                            Box(
                                                modifier = Modifier
                                                    .size(width = 64.dp, height = 72.dp)
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .background(bgColor)
                                                    .border(if (isCurrent) 2.dp else 1.dp, borderColor, RoundedCornerShape(12.dp)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = charValue,
                                                    style = MaterialTheme.typography.headlineSmall,
                                                    fontWeight = FontWeight.Bold,
                                                    color = TextPrimary
                                                )
                                            }
                                        } else {
                                            Box(
                                                modifier = Modifier
                                                    .size(width = 64.dp, height = 72.dp)
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .background(BgSurface)
                                                    .border(1.dp, BorderColor, RoundedCornerShape(12.dp)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = char.toString(),
                                                    style = MaterialTheme.typography.headlineSmall,
                                                    fontWeight = FontWeight.Bold,
                                                    color = TextPrimary.copy(alpha = 0.4f)
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            if (state.isCorrect == false) {
                                Spacer(Modifier.height(16.dp))
                                Text(
                                    text = "정답은 '${quiz.answer}' 입니다.",
                                    color = WrongRed,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 16.sp,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }

                        Spacer(Modifier.height(16.dp))
                    }

                    val isSubjective = quiz.options.isEmpty()
                    val hasSubmittedSubjective = state.isCorrect != null
                    val hasSelectedObjective = state.selectedOption != null
                    val isLastQuestion = state.quizCount >= TOTAL
                    
                    val buttonText = when {
                        isSubjective && !hasSubmittedSubjective -> "정답 확인"
                        isLastQuestion -> "결과 보기 →"
                        else -> "다음 문제 →"
                    }

                    val buttonEnabled = when {
                        isSubjective && !hasSubmittedSubjective -> state.inputText.length == quiz.blankIndices.size
                        else -> (hasSelectedObjective || hasSubmittedSubjective) && animationFile == null
                    }

                    Button(
                        onClick = { 
                            if (isSubjective && !hasSubmittedSubjective) {
                                viewModel.handleIntent(QuizIntent.SubmitAnswer)
                            } else {
                                viewModel.handleIntent(QuizIntent.NextQuiz)
                            }
                        },
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
                            text = buttonText,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
        
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 120.dp)
                .padding(horizontal = 24.dp)
        ) { data ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = BgDark.copy(alpha = 0.95f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Text(
                    text = data.visuals.message,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

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
