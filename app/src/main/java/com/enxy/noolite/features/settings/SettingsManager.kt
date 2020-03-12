package com.enxy.noolite.features.settings

import com.enxy.noolite.BuildConfig
import com.enxy.noolite.core.network.ConnectionManager
import com.enxy.noolite.core.platform.FileManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsManager @Inject constructor(
    private val connectionManager: ConnectionManager,
    private val fileManager: FileManager
) {
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

    var wifiNotification: Boolean = FileManager.DEFAULT_WIFI_NOTIFICATION_VALUE
        set(value) {
            connectionManager.isEnabled = value
            field = value
            fileManager.saveBooleanToPrefs(
                FileManager.SETTINGS_FILE,
                FileManager.WIFI_NOTIFICATION_KEY,
                value
            )
        }

    // Settings for seamless theme change
    var themeChanged: Boolean = false

    var scrollX: Int = 0

    var scrollY: Int = 0

    val appVersion: String = "v${BuildConfig.VERSION_NAME}"

    val appBuildNumber: String = BuildConfig.VERSION_CODE.toString()

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
        this.wifiNotification =
            fileManager.getBooleanFromPrefs(
                FileManager.SETTINGS_FILE,
                FileManager.WIFI_NOTIFICATION_KEY
            )
        this.connectionManager.isEnabled = this.wifiNotification
    }
}