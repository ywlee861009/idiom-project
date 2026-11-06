package com.kero.idiom

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
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
        if (enabled && ReminderManager.canDeliver(context)) {
            ReminderManager.scheduleReminder(context)
        } else {
            ReminderManager.cancelReminder(context)
        }
    }
}

actual fun clearStudyReminder() {
    IdiomApplication.instance?.let(ReminderManager::clearNotification)
}

@Composable
actual fun rememberStudyReminderPermissionRequest(onResult: (Boolean) -> Unit): () -> Unit {
    val context = LocalContext.current
    val handleResult by rememberUpdatedState(onResult)
    var requesting by remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { allowed ->
        requesting = false
        handleResult(allowed && ReminderManager.canDeliver(context))
    }
    return {
        if (!requesting) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) !=
                    PackageManager.PERMISSION_GRANTED
            ) {
                requesting = true
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                handleResult(ReminderManager.canDeliver(context))
            }
        }
    }
}

@Composable
actual fun rememberSystemNotificationEnabled(): Boolean {
    val context = LocalContext.current
    val lifecycle = (context as? LifecycleOwner)?.lifecycle ?: MainActivity.currentActivity?.lifecycle
    var enabled by remember(context) { mutableStateOf(ReminderManager.canDeliver(context)) }
    DisposableEffect(context, lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) enabled = ReminderManager.canDeliver(context)
        }
        lifecycle?.addObserver(observer)
        onDispose { lifecycle?.removeObserver(observer) }
    }
    return enabled
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

actual fun getAppVersion(): String = BuildConfig.VERSION_NAME
