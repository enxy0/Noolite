package com.enxy.noolite.data.db.settings

import com.enxy.noolite.data.db.settings.model.SettingsEntity
import com.enxy.noolite.domain.features.settings.SettingsDbDataSource
import com.enxy.noolite.domain.features.settings.model.AppSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class SettingsDbDataSourceImpl(
    private val settingsDao: SettingsDao
) : SettingsDbDataSource {
    override fun getAppSettingsFlow(): Flow<AppSettings> {
        return settingsDao.getSettingsFlow()
            .map { entity -> entity?.toDomain() ?: AppSettings.default() }
    }

    override suspend fun updateAppSettings(settings: AppSettings) {
        settingsDao.insertSettings(settings.toEntity())
    }

    private fun AppSettings.toEntity() = SettingsEntity(
        apiUrl = apiUrl,
        notifyWifiChange = notifyWifiChange,
        darkTheme = theme.name
    )

    private fun SettingsEntity.toDomain() = AppSettings(
        apiUrl = apiUrl,
        notifyWifiChange = notifyWifiChange,
        theme = AppSettings.Theme.valueOf(darkTheme)
    )
}
