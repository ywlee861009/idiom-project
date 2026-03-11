package com.kero.idiom

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.core.app.NotificationManagerCompat
import com.kero.idiom.notification.ReminderManager
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
        Toast.makeText(context, "화면 설정에서 '글자 크기'를 조절해 주세요.", Toast.LENGTH_LONG).show()

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
            val webIntent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(webIntent)
        }
    }
}

actual fun openBrowser(url: String) {
    IdiomApplication.instance?.let { context ->
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "브라우저를 열 수 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}

actual fun updateReminderSettings(enabled: Boolean) {
    IdiomApplication.instance?.let { context ->
        if (enabled) {
            ReminderManager.scheduleReminder(context)
        } else {
            ReminderManager.cancelReminder(context)
        }
    }
}

actual fun isSystemNotificationEnabled(): Boolean {
    return IdiomApplication.instance?.let {
        NotificationManagerCompat.from(it).areNotificationsEnabled()
    } ?: false
}

actual fun openNotificationSettings() {
    IdiomApplication.instance?.let { context ->
        val intent = Intent().apply {
            when {
                android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O -> {
                    action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                    putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                }
                else -> {
                    action = "android.settings.APP_NOTIFICATION_SETTINGS"
                    putExtra("app_package", context.packageName)
                    putExtra("app_uid", context.applicationInfo.uid)
                }
            }
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            val fallbackIntent = Intent(Settings.ACTION_SETTINGS).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(fallbackIntent)
        }
    }
}

actual fun exitApp() {
    exitProcess(0)
}

actual fun currentMillis(): Long = System.currentTimeMillis()
