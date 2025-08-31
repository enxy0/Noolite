package com.enxy.noolite.domain.settings

import com.enxy.noolite.core.model.AppSettings
import com.enxy.noolite.core.model.Group
import kotlinx.coroutines.flow.Flow

interface SettingsDataSource {
    suspend fun getGroups(): List<Group>
    fun getAppSettingsFlow(): Flow<AppSettings>
    suspend fun updateAppSettings(settings: AppSettings)
}
