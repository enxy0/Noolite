package com.enxy.noolite.data.db.settings

import com.enxy.noolite.data.db.settings.model.SettingsEntity
import com.enxy.noolite.domain.features.settings.SettingsDbDataSource
import com.enxy.noolite.domain.features.settings.model.AppSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

internal class SettingsDbDataSourceImpl(
    private val settingsDao: SettingsDao
) : SettingsDbDataSource {
    override fun getAppSettingsFlow(): Flow<AppSettings> = settingsDao.getSettingsFlow().mapNotNull { entity ->
        if (entity == null) {
            updateAppSettings(AppSettings.default())
        }
        entity?.toDomain()
    }

    override suspend fun updateAppSettings(settings: AppSettings) {
        settingsDao.insertSettings(settings.toEntity())
    }

    private fun AppSettings.toEntity() = SettingsEntity(
        apiUrl = apiUrl,
        notifyWifiChange = notifyWifiChange,
        darkTheme = darkTheme.name
    )

    private fun SettingsEntity.toDomain() = AppSettings(
        apiUrl = apiUrl,
        notifyWifiChange = notifyWifiChange,
        darkTheme = AppSettings.Theme.valueOf(darkTheme)
    )
}
