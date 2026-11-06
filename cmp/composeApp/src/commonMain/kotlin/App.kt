package com.kero.idiom

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import androidx.navigation.toRoute
import com.kero.idiom.core.components.CultureLoadingScreen
import com.kero.idiom.core.components.IdiomTab
import com.kero.idiom.core.navigation.Screen
import com.kero.idiom.core.theme.IdiomQuizTheme
import com.kero.idiom.core.util.Logger
import com.kero.idiom.domain.repository.IdiomRepository
import com.kero.idiom.domain.repository.TodayLessonRepository
import com.kero.idiom.feature.quiz.ui.QuizScreen
import com.kero.idiom.ui.CollectionScreen
import com.kero.idiom.ui.HomeScreen
import com.kero.idiom.ui.ProfileScreen
import com.kero.idiom.ui.RewardScreen
import com.kero.idiom.ui.today.TodayLessonScreen
import com.kero.idiom.ui.today.TodayLessonUnavailable
import com.kero.idiom.ui.today.currentLessonDay
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import org.koin.compose.KoinContext
import org.koin.compose.koinInject

@Composable
fun App(
    openTodayLesson: Boolean = false,
    onTodayLessonOpened: () -> Unit = {}
) {
    IdiomQuizTheme {
        KoinContext {
            val repository: IdiomRepository = koinInject()
            var localReady by remember { mutableStateOf(false) }
            var localFailed by remember { mutableStateOf(false) }
            var localAttempt by remember { mutableIntStateOf(0) }

            LaunchedEffect(localAttempt) {
                try {
                    repository.initializeLocal()
                    localReady = true
                } catch (cancelled: CancellationException) {
                    throw cancelled
                } catch (_: Exception) {
                    localFailed = true
                }
            }

            if (!localReady) {
                if (localFailed) {
                    TodayLessonUnavailable(
                        message = "학습 자료를 열지 못했어요.",
                        onRetry = {
                            localFailed = false
                            localAttempt++
                        }
                    )
                } else {
                    CultureLoadingScreen(message = "서책을 정리 중입니다...")
                }
            } else {
                val navController = rememberNavController()
                val navigationScope = rememberCoroutineScope()
                val lessons: TodayLessonRepository = koinInject()

                LaunchedEffect(Unit) {
                    try {
                        repository.syncIfNeeded()
                    } catch (cancelled: CancellationException) {
                        throw cancelled
                    } catch (error: Exception) {
                        Logger.e("Failed to refresh the idiom catalog", error)
                    }
                }

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
                            onOpenTodayLesson = {
                                navigationScope.launch { navController.openTodayLesson(lessons) }
                            }
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
                            onNavigateToResult = { score, total, xpGained ->
                                navController.navigate(Screen.Reward(score, total, xpGained)) {
                                    popUpTo(Screen.Quiz) { inclusive = true }
                                }
                            },
                            onNavigateBack = {
                                navController.popBackStack()
                            }
                        )
                    }

                    composable<Screen.TodayLesson> { backStackEntry ->
                        TodayLessonScreen(
                            day = backStackEntry.toRoute<Screen.TodayLesson>().day,
                            onBack = { navController.popBackStack() },
                            onCollection = {
                                navController.navigate(Screen.Collection) {
                                    popUpTo(Screen.Home) { inclusive = false }
                                    launchSingleTop = true
                                }
                            }
                        )
                    }

                    composable<Screen.Reward> { backStackEntry ->
                        val reward = backStackEntry.toRoute<Screen.Reward>()
                        RewardScreen(
                            score = reward.score,
                            total = reward.total,
                            xpGained = reward.xpGained,
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

                LaunchedEffect(openTodayLesson) {
                    if (openTodayLesson) {
                        navController.openTodayLesson(lessons)
                        onTodayLessonOpened()
                    }
                }
            }
        }
    }
}

private suspend fun NavHostController.openTodayLesson(lessons: TodayLessonRepository) {
    val day = currentLessonDay()
    try {
        val lesson = lessons.get(day)
        // An explicit re-entry after completion opens the record. The live quiz still shows its final explanation.
        if (lesson != null && lesson.isCompleted && !lesson.isResultVisible) {
            lessons.advance(day, lesson.currentIndex)
        }
    } catch (cancelled: CancellationException) {
        throw cancelled
    } catch (error: Exception) {
        Logger.e("Failed to open the saved daily lesson result", error)
    }

    navigate(Screen.TodayLesson(day)) {
        popUpTo(Screen.Home) { inclusive = false }
        launchSingleTop = true
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
