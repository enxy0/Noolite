package com.enxy.noolite.features.settings

sealed interface SettingsSideEffect {
    data class Message(val message: String) : SettingsSideEffect
}
