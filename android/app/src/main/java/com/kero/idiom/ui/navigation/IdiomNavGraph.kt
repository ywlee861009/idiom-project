package com.kero.idiom.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kero.idiom.core.navigation.Destination
import com.kero.idiom.feature.quiz.ui.QuizScreen
import com.kero.idiom.feature.result.ui.ResultScreen

@Composable
fun IdiomNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Destination.Quiz
    ) {
        composable<Destination.Quiz> {
            QuizScreen(
                onNavigateToResult = { score ->
                    navController.navigate(Destination.Result(score))
                }
            )
        }
        composable<Destination.Result> { backStackEntry ->
            val result = backStackEntry.toRoute<Destination.Result>()
            ResultScreen(
                score = result.score,
                onNavigateToQuiz = {
                    navController.navigate(Destination.Quiz) {
                        popUpTo(Destination.Quiz) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Destination.Quiz) {
                        popUpTo(Destination.Quiz) { inclusive = true }
                    }
                }
            )
        }
    }
}
