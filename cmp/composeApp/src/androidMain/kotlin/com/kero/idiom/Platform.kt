package com.kero.idiom

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import kotlin.system.exitProcess

@Composable
actual fun BackPressHandler(onBack: () -> Unit) {
    BackHandler(onBack = onBack)
}

actual fun showToast(message: String) {
    IdiomApplication.instance?.let {
        Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
    }
}

actual fun openFontSizeSettings() {
    IdiomApplication.instance?.let { context ->
        // 1. 안내 메시지 표시 (어르신들을 위한 배려)
        Toast.makeText(context, "화면 설정에서 '글자 크기'를 조절해 주세요.", Toast.LENGTH_LONG).show()

        // 2. 최대한 구체적인 폰트 설정 화면 시도
        val intent = try {
            Intent("android.settings.FONT_SIZE").apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        } catch (e: Exception) {
            Intent(Settings.ACTION_DISPLAY_SETTINGS).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        }
        
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            val fallbackIntent = Intent(Settings.ACTION_DISPLAY_SETTINGS).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(fallbackIntent)
        }
    }
}

actual fun openStorePage() {
    IdiomApplication.instance?.let { context ->
        val packageName = context.packageName
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("market://details?id=$packageName")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            // 플레이 스토어 앱이 없는 경우 브라우저로 열기
            val webIntent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(webIntent)
        }
    }
}

actual fun exitApp() {
    exitProcess(0)
}

actual fun currentMillis(): Long = System.currentTimeMillis()
