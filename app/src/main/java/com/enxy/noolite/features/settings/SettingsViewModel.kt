package com.enxy.noolite.features.settings

import androidx.lifecycle.ViewModel
import com.enxy.noolite.core.platform.FileManager
import javax.inject.Inject

class SettingsViewModel @Inject constructor(private val settingsManager: SettingsManager) :
    ViewModel() {
    val hasToggleButton: Boolean
        get() = settingsManager.hasToggleButton
    val ipAddress: String
        get() = settingsManager.ipAddress
    val currentTheme: String
        get() = settingsManager.currentTheme
    val wifiNotification: Boolean
        get() = settingsManager.wifiNotification
    val appBuildNumber: String
        get() = settingsManager.appBuildNumber
    val appVersion: String
        get() = settingsManager.appVersion
    val themeChanged: Boolean
        get() = settingsManager.themeChanged
    val scrollX: Int
        get() = settingsManager.scrollX
    val scrollY: Int
        get() = settingsManager.scrollY

    fun setHasToggleButton(isChecked: Boolean) {
        settingsManager.hasToggleButton = isChecked
    }

    fun setIpAddress(ipAddress: String) {
        settingsManager.ipAddress = ipAddress
    }

    fun setLightTheme() {
        settingsManager.currentTheme = FileManager.WHITE_BLUE_THEME_VALUE
    }

    fun setDarkTheme() {
        settingsManager.currentTheme = FileManager.DARK_GREEN_THEME_VALUE
    }

    fun setBlackTheme() {
        settingsManager.currentTheme = FileManager.BLACK_BLUE_THEME_VALUE
    }

    fun setWifiNotification(isChecked: Boolean) {
        settingsManager.wifiNotification = isChecked
    }

    fun clearThemeChangeValues() = with(settingsManager) {
        themeChanged = false
        scrollX = 0
        scrollY = 0
    }
}