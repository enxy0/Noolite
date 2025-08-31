package com.enxy.noolite.feature.settings.theme

import com.arkivanov.decompose.ComponentContext
import com.enxy.noolite.core.model.AppSettings
import org.koin.core.component.KoinComponent

internal interface ChangeThemeComponent {
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
