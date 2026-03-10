package com.kero.idiom

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kero.idiom.core.components.CultureLoadingScreen
import com.kero.idiom.core.theme.IdiomQuizTheme
import com.kero.idiom.domain.repository.IdiomRepository
import com.kero.idiom.feature.quiz.ui.QuizScreen
import com.kero.idiom.feature.result.ui.ResultScreen
import kotlinx.coroutines.delay
import org.koin.compose.KoinContext
import org.koin.compose.koinInject

@Composable
fun App() {
    IdiomQuizTheme {
        KoinContext { // Koin 의존성 주입 컨텍스트
            val repository: IdiomRepository = koinInject()
            var isSyncing by remember { mutableStateOf(true) }

            LaunchedEffect(Unit) {
                repository.syncIfNeeded()
                delay(2000) // 최소 2초 대기
                isSyncing = false
            }

            if (isSyncing) {
                CultureLoadingScreen()
            } else {
                val navController = rememberNavController()
                
                NavHost(
                    navController = navController,
                    startDestination = "quiz"
                ) {
                    composable("quiz") {
                        QuizScreen(
                            onNavigateToResult = { score, total ->
                                navController.navigate("result/$score/$total")
                            }
                        )
                    }
                    composable("result/{score}/{total}") { backStackEntry ->
                        val score = backStackEntry.arguments?.getString("score")?.toIntOrNull() ?: 0
                        val total = backStackEntry.arguments?.getString("total")?.toIntOrNull() ?: 0
                        ResultScreen(
                            score = score,
                            total = total,
                            onRestart = {
                                navController.navigate("quiz") {
                                    popUpTo("quiz") { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
