package com.enxy.noolite.di.modules

import com.enxy.noolite.presentation.ui.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

internal val settingsModule = module {
    viewModelOf(::SettingsViewModel)
}
