package com.kero.idiom.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kero.idiom.Greeting // Placeholder

@Composable
fun IdiomNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Destination.Quiz
    ) {
        composable<Destination.Quiz> {
            Greeting(name = "Quiz Screen")
        }
        composable<Destination.Result> { backStackEntry ->
            val result = backStackEntry.toRoute<Destination.Result>()
            Greeting(name = "Result Screen: ${result.score}")
        }
    }
}
