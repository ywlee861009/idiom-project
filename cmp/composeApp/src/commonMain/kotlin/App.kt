package com.kero.idiom

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kero.idiom.core.theme.IdiomQuizTheme
import com.kero.idiom.feature.quiz.ui.QuizScreen
import com.kero.idiom.feature.result.ui.ResultScreen
import org.koin.compose.KoinContext

@Composable
fun App() {
    IdiomQuizTheme {
        KoinContext { // Koin 의존성 주입 컨텍스트
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
