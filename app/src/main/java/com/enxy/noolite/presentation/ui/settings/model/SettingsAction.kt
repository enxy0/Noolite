package com.enxy.noolite.presentation.ui.settings.model

sealed class SettingsAction {
    object None : SettingsAction()
    data class ShowSnackbar(val message: String, val isFailure: Boolean) : SettingsAction()
}
