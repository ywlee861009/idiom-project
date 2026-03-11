package com.kero.idiom

import androidx.compose.runtime.Composable

@Composable
expect fun BackPressHandler(onBack: () -> Unit)

expect fun showToast(message: String)

expect fun openFontSizeSettings()

expect fun openStorePage()

expect fun openBrowser(url: String)

expect fun updateReminderSettings(enabled: Boolean)

expect fun exitApp()

expect fun currentMillis(): Long
