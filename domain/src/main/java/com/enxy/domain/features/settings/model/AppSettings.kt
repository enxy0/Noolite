package com.enxy.domain.features.settings.model

import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatDelegate

data class AppSettings(
    val apiUrl: String,
    val darkTheme: Theme = Theme.SYSTEM,
    val notifyWifiChange: Boolean = true
) {
    val isDarkTheme: Boolean
        get() = when (darkTheme) {
            Theme.DARK -> true
            else -> false
        }

    companion object {
        fun default() = AppSettings(
            apiUrl = "192.168.0.10",
            darkTheme = Theme.SYSTEM,
            notifyWifiChange = true
        )
    }

    @Keep
    enum class Theme {
        SYSTEM,
        LIGHT,
        DARK;

        companion object {
            fun from(darkTheme: Boolean) = when {
                darkTheme -> DARK
                else -> LIGHT
            }
        }

        fun getNightMode() = when (this) {
            SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            DARK -> AppCompatDelegate.MODE_NIGHT_YES
        }
    }
}
