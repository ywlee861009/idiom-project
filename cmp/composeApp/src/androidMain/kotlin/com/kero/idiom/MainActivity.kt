package com.kero.idiom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 최신 방식: OnBackPressedDispatcher를 사용하여 뒤로가기 시 앱 종료 처리
        onBackPressedDispatcher.addCallback(this) {
            finish()
        }
        
        setContent {
            App() // 공통 UI 호출!
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
