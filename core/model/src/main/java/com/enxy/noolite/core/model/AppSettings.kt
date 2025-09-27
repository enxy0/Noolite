package com.enxy.noolite.core.model

import kotlinx.serialization.Serializable

@Serializable
data class AppSettings(
    val apiUrl: String,
    val theme: Theme = Theme.SYSTEM,
    val notifyWifiChange: Boolean = true,
) {
    companion object {
        fun default() = AppSettings(
            apiUrl = "192.168.0.10",
            theme = Theme.SYSTEM,
            notifyWifiChange = true,
        )
    }

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
    }
}
