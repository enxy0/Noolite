package com.enxy.noolite.features.settings.model

sealed class SettingsAction {
    data class ShowSnackbar(val message: String, val isFailure: Boolean) : SettingsAction()
}
