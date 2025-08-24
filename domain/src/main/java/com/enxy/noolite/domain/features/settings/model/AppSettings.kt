package com.enxy.noolite.domain.features.settings.model

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
