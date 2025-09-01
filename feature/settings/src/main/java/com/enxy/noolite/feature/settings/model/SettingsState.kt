package com.enxy.noolite.feature.settings.model

import androidx.compose.runtime.Immutable
import com.enxy.noolite.core.model.AppSettings
import com.enxy.noolite.core.model.AppSettings.Theme

@Immutable
data class SettingsState(
    val apiUrl: String,
    val apiUrlChanging: Boolean,
    val theme: Theme,
    val appVersion: String,
) {

    companion object {
        fun empty() = SettingsState(
            settings = AppSettings.default(),
            apiUrlChanging = false,
            appVersion = "",
        )
    }

    constructor(
        settings: AppSettings,
        apiUrlChanging: Boolean,
        appVersion: String,
    ) : this(
        apiUrl = settings.apiUrl,
        apiUrlChanging = apiUrlChanging,
        theme = settings.theme,
        appVersion = appVersion,
    )

    fun copy(settings: AppSettings) = copy(
        apiUrl = settings.apiUrl,
        theme = settings.theme,
    )
}

fun SettingsState.toAppSettings() = AppSettings(
    apiUrl = apiUrl,
    theme = theme,
)
