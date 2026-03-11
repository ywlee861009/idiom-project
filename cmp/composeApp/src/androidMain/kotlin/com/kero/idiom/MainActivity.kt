package com.kero.idiom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.ads.MobileAds
import com.kero.idiom.core.ads.AdController
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val adController: AdController by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentActivity = this
        
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
