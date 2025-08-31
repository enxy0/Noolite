package com.enxy.noolite.feature.settings

sealed interface SettingsSideEffect {
    data class Message(val message: String) : SettingsSideEffect
}
