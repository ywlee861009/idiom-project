package com.kero.idiom

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

actual fun exitApp() {
    exitProcess(0)
}

actual fun currentMillis(): Long = System.currentTimeMillis()
