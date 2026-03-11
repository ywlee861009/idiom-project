package com.kero.idiom

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.MobileAds
import com.kero.idiom.core.ads.AdController
import com.kero.idiom.domain.repository.UserStatsRepository
import com.kero.idiom.notification.ReminderManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val adController: AdController by inject()
    private val userStatsRepository: UserStatsRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentActivity = this

        // 유저 설정에 따른 알림 예약 여부 결정
        runBlocking {
            launch {
                val stats = userStatsRepository.getUserStats().first()
                if (stats.isNotificationEnabled) {
                    ReminderManager.scheduleReminder(this@MainActivity)
                } else {
                    ReminderManager.cancelReminder(this@MainActivity)
                }
            }
        }

        // 안드로이드 13 이상 알림 권한 요청
        requestNotificationPermission()

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
            App() // 공통 UI 호출!
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                    if (isGranted) {
                        // 권한 허용 시 리마인더 예약 (한 번 더 확실하게)
                        ReminderManager.scheduleReminder(this)
                    }
                }
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    override fun onDestroy() {
        if (currentActivity == this) {
            currentActivity = null
        }
        super.onDestroy()
    }

    companion object {
        var currentActivity: MainActivity? = null
            private set
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
