package com.kero.idiom.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kero.idiom.core.navigation.Destination
import com.kero.idiom.feature.result.ui.ResultScreen

@Composable
fun IdiomNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Destination.Quiz // route String 대신 Destination 객체 전달
    ) {
        composable<Destination.Quiz> {
            Text(text = "Quiz Screen Placeholder")
        }
        composable<Destination.Result> { backStackEntry ->
            // 타입 세이프하게 파라미터 추출
            val result = backStackEntry.toRoute<Destination.Result>()
            ResultScreen(score = result.score)
        }
    }
}
