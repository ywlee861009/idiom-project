package com.kero.idiom

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kero.idiom.core.components.CultureLoadingScreen
import com.kero.idiom.core.components.IdiomTab
import com.kero.idiom.core.theme.IdiomQuizTheme
import com.kero.idiom.domain.repository.IdiomRepository
import com.kero.idiom.feature.quiz.ui.QuizScreen
import com.kero.idiom.ui.CollectionScreen
import com.kero.idiom.ui.HomeScreen
import com.kero.idiom.ui.ProfileScreen
import com.kero.idiom.ui.RewardScreen
import kotlinx.coroutines.delay
import org.koin.compose.KoinContext
import org.koin.compose.koinInject

@Composable
fun App() {
    IdiomQuizTheme {
        KoinContext {
            val repository: IdiomRepository = koinInject()
            var isSyncing by remember { mutableStateOf(true) }

            LaunchedEffect(Unit) {
                repository.syncIfNeeded()
                delay(2000)
                isSyncing = false
            }

            if (isSyncing) {
                CultureLoadingScreen()
            } else {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {
                    composable("home") {
                        HomeScreen(
                            onTabSelected = { tab ->
                                when (tab) {
                                    IdiomTab.Home -> {}
                                    IdiomTab.Quiz -> navController.navigate("quiz")
                                    IdiomTab.Study -> navController.navigate("collection") {
                                        launchSingleTop = true
                                    }
                                }
                            },
                            onStartQuiz = { navController.navigate("quiz") },
                            onNavigateToProfile = { navController.navigate("profile") }
                        )
                    }

                    composable("collection") {
                        CollectionScreen(
                            onTabSelected = { tab ->
                                when (tab) {
                                    IdiomTab.Home -> navController.navigate("home") {
                                        popUpTo("home") { inclusive = true }
                                        launchSingleTop = true
                                    }
                                    IdiomTab.Quiz -> navController.navigate("quiz")
                                    IdiomTab.Study -> {}
                                }
                            }
                        )
                    }

                    composable("profile") {
                        ProfileScreen(
                            onTabSelected = { tab ->
                                when (tab) {
                                    IdiomTab.Home -> navController.navigate("home") {
                                        popUpTo("home") { inclusive = true }
                                        launchSingleTop = true
                                    }
                                    IdiomTab.Quiz -> navController.navigate("quiz")
                                    IdiomTab.Study -> navController.navigate("collection") {
                                        launchSingleTop = true
                                    }
                                }
                            }
                        )
                    }

                    composable("quiz") {
                        QuizScreen(
                            onNavigateToResult = { score, total ->
                                navController.navigate("reward/$score/$total") {
                                    popUpTo("quiz") { inclusive = true }
                                }
                            },
                            onNavigateBack = {
                                navController.popBackStack()
                            }
                        )
                    }

                    composable("reward/{score}/{total}") { backStackEntry ->
                        val score = backStackEntry.arguments?.getString("score")?.toIntOrNull() ?: 0
                        val total = backStackEntry.arguments?.getString("total")?.toIntOrNull() ?: 0
                        RewardScreen(
                            score = score,
                            total = total,
                            onNavigateToCollection = {
                                navController.navigate("collection") {
                                    popUpTo("home") { inclusive = false }
                                }
                            },
                            onNavigateToHome = {
                                navController.navigate("home") {
                                    popUpTo("home") { inclusive = true }
                                    launchSingleTop = true
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
