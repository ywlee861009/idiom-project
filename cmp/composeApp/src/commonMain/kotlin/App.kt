package com.kero.idiom

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kero.idiom.core.components.CultureLoadingScreen
import com.kero.idiom.core.components.IdiomTab
import com.kero.idiom.core.navigation.Screen
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
                    startDestination = Screen.Home
                ) {
                    composable<Screen.Home> {
                        HomeScreen(
                            onTabSelected = { tab ->
                                when (tab) {
                                    IdiomTab.Home -> {}
                                    IdiomTab.Quiz -> navController.navigate(Screen.Quiz)
                                    IdiomTab.Study -> navController.navigate(Screen.Collection) {
                                        launchSingleTop = true
                                    }
                                }
                            },
                            onStartQuiz = { navController.navigate(Screen.Quiz) },
                            onNavigateToProfile = { navController.navigate(Screen.Profile) }
                        )
                    }

                    composable<Screen.Collection> {
                        CollectionScreen(
                            onTabSelected = { tab ->
                                when (tab) {
                                    IdiomTab.Home -> navController.navigate(Screen.Home) {
                                        popUpTo(Screen.Home) { inclusive = true }
                                        launchSingleTop = true
                                    }
                                    IdiomTab.Quiz -> navController.navigate(Screen.Quiz)
                                    IdiomTab.Study -> {}
                                }
                            }
                        )
                    }

                    composable<Screen.Profile> {
                        ProfileScreen(
                            onTabSelected = { tab ->
                                when (tab) {
                                    IdiomTab.Home -> navController.navigate(Screen.Home) {
                                        popUpTo(Screen.Home) { inclusive = true }
                                        launchSingleTop = true
                                    }
                                    IdiomTab.Quiz -> navController.navigate(Screen.Quiz)
                                    IdiomTab.Study -> navController.navigate(Screen.Collection) {
                                        launchSingleTop = true
                                    }
                                }
                            }
                        )
                    }

                    composable<Screen.Quiz> {
                        QuizScreen(
                            onNavigateToResult = { score, total ->
                                navController.navigate(Screen.Reward(score, total)) {
                                    popUpTo(Screen.Quiz) { inclusive = true }
                                }
                            },
                            onNavigateBack = {
                                navController.popBackStack()
                            }
                        )
                    }

                    composable<Screen.Reward> { backStackEntry ->
                        val reward = backStackEntry.toRoute<Screen.Reward>()
                        RewardScreen(
                            score = reward.score,
                            total = reward.total,
                            onNavigateToCollection = {
                                navController.navigate(Screen.Collection) {
                                    popUpTo(Screen.Home) { inclusive = false }
                                }
                            },
                            onNavigateToHome = {
                                navController.navigate(Screen.Home) {
                                    popUpTo(Screen.Home) { inclusive = true }
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
