package com.enxy.data.db.settings.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Settings")
internal data class SettingsEntity(
    @PrimaryKey
    val id: Int = 0,
    @ColumnInfo(name = "api_url")
    val apiUrl: String,
    @ColumnInfo(name = "notify_wifi_change")
    val notifyWifiChange: Boolean,
    @ColumnInfo(name = "dark_theme")
    val darkTheme: String
)
