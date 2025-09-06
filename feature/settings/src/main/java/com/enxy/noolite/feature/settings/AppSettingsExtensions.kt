package com.enxy.noolite.feature.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.enxy.noolite.core.model.AppSettings

@Composable
internal fun AppSettings.Theme.asString(): String = when (this) {
    AppSettings.Theme.LIGHT -> stringResource(R.string.settings_theme_light)
    AppSettings.Theme.DARK -> stringResource(R.string.settings_theme_dark)
    AppSettings.Theme.SYSTEM -> stringResource(R.string.settings_theme_system)
}
