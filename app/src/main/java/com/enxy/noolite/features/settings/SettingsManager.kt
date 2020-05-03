package com.enxy.noolite.features.settings

import com.enxy.noolite.BuildConfig
import com.enxy.noolite.core.network.ConnectionManager
import com.enxy.noolite.core.utils.Constants.Companion.DEFAULT_IP_ADDRESS_VALUE
import com.enxy.noolite.core.utils.Constants.Companion.DEFAULT_THEME_NAME
import com.enxy.noolite.core.utils.Constants.Companion.DEFAULT_WIFI_NOTIFICATION_VALUE
import com.enxy.noolite.core.utils.Constants.Companion.IP_ADDRESS_KEY
import com.enxy.noolite.core.utils.Constants.Companion.SETTINGS_FILE
import com.enxy.noolite.core.utils.Constants.Companion.THEME_KEY
import com.enxy.noolite.core.utils.Constants.Companion.TOGGLE_BUTTON_KEY
import com.enxy.noolite.core.utils.Constants.Companion.WIFI_NOTIFICATION_KEY
import com.enxy.noolite.core.utils.FileManager

/**
 * [SettingsManager] manages app settings. It's a singleton which stores and saves app values.
 * Only [SettingsManager] allowed to save values of its properties.
 */
class SettingsManager(
    private val connectionManager: ConnectionManager,
    private val fileManager: FileManager
) {
    var hasToggleButton: Boolean = true
        set(value) {
            field = value
            fileManager.saveBooleanToPrefs(
                SETTINGS_FILE, TOGGLE_BUTTON_KEY, value
            )
        }

    var ipAddress: String = DEFAULT_IP_ADDRESS_VALUE
        set(value) {
            field = value
            fileManager.saveStringToPrefs(SETTINGS_FILE, IP_ADDRESS_KEY, value)
        }

    var currentTheme: String = DEFAULT_THEME_NAME
        set(value) {
            field = value
            fileManager.saveStringToPrefs(SETTINGS_FILE, THEME_KEY, value)
        }

    var wifiNotification: Boolean = DEFAULT_WIFI_NOTIFICATION_VALUE
        set(value) {
            connectionManager.isEnabled = value
            field = value
            fileManager.saveBooleanToPrefs(SETTINGS_FILE, WIFI_NOTIFICATION_KEY, value)
        }

    var themeChanged: Boolean = false

    var scrollX: Int = 0

    var scrollY: Int = 0

    val appVersion: String = "v${BuildConfig.VERSION_NAME}"

    val appBuildNumber: String = BuildConfig.VERSION_CODE.toString()

    init {
        this.hasToggleButton = fileManager.getBooleanFromPrefs(SETTINGS_FILE, TOGGLE_BUTTON_KEY)
        this.ipAddress = fileManager.getStringFromPrefs(SETTINGS_FILE, IP_ADDRESS_KEY)
            ?: DEFAULT_IP_ADDRESS_VALUE
        this.currentTheme = fileManager.getStringFromPrefs(SETTINGS_FILE, THEME_KEY)
            ?: DEFAULT_THEME_NAME
        this.wifiNotification =
            fileManager.getBooleanFromPrefs(SETTINGS_FILE, WIFI_NOTIFICATION_KEY)
        this.connectionManager.isEnabled = this.wifiNotification
    }
}