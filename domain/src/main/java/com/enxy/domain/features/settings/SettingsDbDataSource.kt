package com.enxy.domain.features.settings

import com.enxy.domain.features.settings.model.AppSettings
import kotlinx.coroutines.flow.Flow

interface SettingsDbDataSource {
    fun getAppSettingsFlow(): Flow<AppSettings>
    suspend fun updateAppSettings(settings: AppSettings)
}
