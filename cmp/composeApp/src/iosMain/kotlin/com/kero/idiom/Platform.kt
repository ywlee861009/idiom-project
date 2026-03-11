package com.kero.idiom

import androidx.compose.runtime.Composable
import platform.Foundation.NSDate
import platform.Foundation.date
import platform.Foundation.timeIntervalSince1970

@Composable
actual fun BackPressHandler(onBack: () -> Unit) {
    // No-op for iOS
}

actual fun showToast(message: String) {
    // No-op for iOS
}

actual fun openFontSizeSettings() {
    // No-op for iOS
}

actual fun openStorePage() {
    // No-op for iOS
}

actual fun exitApp() {
    // No-op for iOS
}

actual fun currentMillis(): Long = (NSDate.date().timeIntervalSince1970 * 1000).toLong()
