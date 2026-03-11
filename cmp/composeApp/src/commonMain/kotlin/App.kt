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
            var syncMessage by remember { mutableStateOf("서책을 정리 중입니다...") }

            LaunchedEffect(Unit) {
                repository.syncIfNeeded { message ->
                    syncMessage = message
                }
                isSyncing = false
            }

            if (isSyncing) {
                CultureLoadingScreen(message = syncMessage)
            } else {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Screen.Home
                ) {
                    composable<Screen.Home> {
                        DoubleBackExitHandler()

                        HomeScreen(
                            onTabSelected = { tab ->
                                when (tab) {
                                    IdiomTab.Home -> {}
                                    IdiomTab.Study -> navController.navigate(Screen.Collection) {
                                        launchSingleTop = true
                                    }
                                    IdiomTab.Profile -> navController.navigate(Screen.Profile) {
                                        launchSingleTop = true
                                    }
                                }
                            },
                            onStartQuiz = { navController.navigate(Screen.Quiz) },
                            onNavigateToProfile = { navController.navigate(Screen.Profile) }
                        )
                    }

                    composable<Screen.Collection> {
                        DoubleBackExitHandler()

                        CollectionScreen(
                            onTabSelected = { tab ->
                                when (tab) {
                                    IdiomTab.Home -> navController.navigate(Screen.Home) {
                                        popUpTo(Screen.Home) { inclusive = true }
                                        launchSingleTop = true
                                    }
                                    IdiomTab.Study -> {}
                                    IdiomTab.Profile -> navController.navigate(Screen.Profile) {
                                        launchSingleTop = true
                                    }
                                }
                            }
                        )
                    }

                    composable<Screen.Profile> {
                        DoubleBackExitHandler()

                        ProfileScreen(
                            onTabSelected = { tab ->
                                when (tab) {
                                    IdiomTab.Home -> navController.navigate(Screen.Home) {
                                        popUpTo(Screen.Home) { inclusive = true }
                                        launchSingleTop = true
                                    }
                                    IdiomTab.Study -> navController.navigate(Screen.Collection) {
                                        launchSingleTop = true
                                    }
                                    IdiomTab.Profile -> {}
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

/**
 * 뒤로가기 버튼을 두 번 누르면 앱을 종료하는 핸들러 컴포저블.
 * 최상위 화면(Home, Collection, Profile)에서 사용됩니다.
 */
@Composable
fun DoubleBackExitHandler() {
    var lastBackPressTime by remember { mutableLongStateOf(0L) }
    
    BackPressHandler {
        val currentTime = currentMillis()
        if (currentTime - lastBackPressTime < 2000) {
            exitApp()
        } else {
            lastBackPressTime = currentTime
            showToast("뒤로가기 버튼을 한 번 더 누르면 종료됩니다.")
        }
    }
}
