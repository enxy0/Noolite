package com.enxy.noolite.core.utils

import com.enxy.noolite.R

/**
 * Constants used throughout the app.
 */
class Constants {
    companion object {
        // File names
        const val SETTINGS_FILE = "settings"
        const val MAIN_DATA_FILE = "main"

        // Settings
        const val IP_ADDRESS_KEY = "ip_address"
        const val WIFI_NOTIFICATION_KEY = "wifi_notification"
        const val FAVOURITE_GROUP_KEY = "favourite_group_element"
        const val GROUP_LIST_KEY = "group_list"
        const val TOGGLE_BUTTON_KEY = "toggle_button"
        const val SCRIPT_LIST_KEY = "script_list"

        // Themes
        const val THEME_KEY = "theme"
        const val WHITE_THEME_VALUE = "white"
        const val DARK_THEME_VALUE = "dark"
        const val BLACK_THEME_VALUE = "black"

        // Default values
        const val DEFAULT_IP_ADDRESS_VALUE = "192.168.0.10"
        const val DEFAULT_THEME_NAME = DARK_THEME_VALUE
        const val DEFAULT_THEME = R.style.AppTheme_Dark_Green
        const val DEFAULT_WIFI_NOTIFICATION_VALUE = true
    }
}