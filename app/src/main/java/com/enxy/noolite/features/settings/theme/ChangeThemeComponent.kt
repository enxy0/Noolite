package com.enxy.noolite.features.settings.theme

import com.arkivanov.decompose.ComponentContext
import com.enxy.noolite.domain.features.settings.model.AppSettings
import org.koin.core.component.KoinComponent

interface ChangeThemeComponent {
    val options: List<AppSettings.Theme>
    fun onThemeChange(theme: AppSettings.Theme)
    fun onDismiss()
}

internal class ChangeThemeComponentImpl(
    componentContext: ComponentContext,
    private val onDismissed: () -> Unit,
    private val onThemeChanged: (theme: AppSettings.Theme) -> Unit
) : ChangeThemeComponent,
    ComponentContext by componentContext,
    KoinComponent {

    override val options: List<AppSettings.Theme> = AppSettings.Theme.entries

    override fun onThemeChange(theme: AppSettings.Theme) {
        onThemeChanged(theme)
    }

    override fun onDismiss() {
        onDismissed()
    }
}
