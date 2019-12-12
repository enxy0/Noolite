package com.enxy.noolite.features

import com.enxy.noolite.core.platform.FileManager
import javax.inject.Inject

class SettingsManager @Inject constructor(val fileManager: FileManager) {
    var hasToggleButton: Boolean = true
        set(value) {
            field = value
            fileManager.saveBooleanToPrefs(
                FileManager.SETTINGS_FILE,
                FileManager.TOGGLE_BUTTON_KEY,
                value
            )
        }

    var ipAddress: String = FileManager.DEFAULT_IP_ADDRESS_VALUE
        set(value) {
            field = value
            fileManager.saveStringToPrefs(
                FileManager.SETTINGS_FILE,
                FileManager.IP_ADDRESS_KEY,
                value
            )
        }

    var currentTheme: String = FileManager.DARK_GREEN_THEME_VALUE
        set(value) {
            field = value
            fileManager.saveStringToPrefs(FileManager.SETTINGS_FILE, FileManager.THEME_KEY, value)
        }

    // Settings for seamless theme change
    var themeChanged: Boolean = false

    var scrollX: Int = 0

    var scrollY: Int = 0

    init {
        this.hasToggleButton = fileManager.getBooleanFromPrefs(
            FileManager.SETTINGS_FILE,
            FileManager.TOGGLE_BUTTON_KEY
        )
        this.ipAddress =
            fileManager.getStringFromPrefs(FileManager.SETTINGS_FILE, FileManager.IP_ADDRESS_KEY)
                ?: FileManager.DEFAULT_IP_ADDRESS_VALUE
        this.currentTheme =
            fileManager.getStringFromPrefs(FileManager.SETTINGS_FILE, FileManager.THEME_KEY)
                ?: FileManager.DARK_GREEN_THEME_VALUE
    }
}