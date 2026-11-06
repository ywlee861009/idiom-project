package com.kero.idiom

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.ads.MobileAds
import com.kero.idiom.core.ads.AdController
import com.kero.idiom.domain.repository.UserStatsRepository
import com.kero.idiom.notification.ReminderManager
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.lang.ref.WeakReference

class MainActivity : ComponentActivity() {

    private val adController: AdController by inject()
    private val userStatsRepository: UserStatsRepository by inject()
    private var openTodayLesson by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentActivityRef = WeakReference(this)
        openTodayLesson = savedInstanceState?.getBoolean(STATE_OPEN_TODAY_LESSON)
            ?: (intent.action == ReminderManager.ACTION_OPEN_TODAY_LESSON)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                userStatsRepository.getUserStats()
                    .map { it.isNotificationEnabled }
                    .distinctUntilChanged()
                    .collect { updateReminderSettings(it) }
            }
        }

        // AdMob 초기화
        MobileAds.initialize(this) {}
        
        // 광고 미리 로드
        adController.loadInterstitial()

        enableEdgeToEdge()

        // 최신 방식: OnBackPressedDispatcher를 사용하여 뒤로가기 시 앱 종료 처리
        onBackPressedDispatcher.addCallback(this) {
            finish()
        }
        
        setContent {
            App(
                openTodayLesson = openTodayLesson,
                onTodayLessonOpened = {
                    openTodayLesson = false
                    if (intent.action == ReminderManager.ACTION_OPEN_TODAY_LESSON) {
                        setIntent(Intent(intent).setAction(Intent.ACTION_MAIN))
                    }
                }
            )
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        if (intent.action == ReminderManager.ACTION_OPEN_TODAY_LESSON) openTodayLesson = true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(STATE_OPEN_TODAY_LESSON, openTodayLesson)
    }

    override fun onDestroy() {
        if (currentActivityRef?.get() == this) {
            currentActivityRef = null
        }
        super.onDestroy()
    }

    companion object {
        private const val STATE_OPEN_TODAY_LESSON = "open_today_lesson"

        private var currentActivityRef: WeakReference<MainActivity>? = null
        val currentActivity: MainActivity?
            get() = currentActivityRef?.get()
    }
}
