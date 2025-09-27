package com.enxy.noolite.data.settings

import com.enxy.noolite.core.database.settings.SettingsDao
import com.enxy.noolite.core.database.settings.model.SettingsEntity
import com.enxy.noolite.core.model.AppSettings
import com.enxy.noolite.core.model.Group
import com.enxy.noolite.core.network.api.NooliteApi
import com.enxy.noolite.core.network.api.NooliteBinParser
import com.enxy.noolite.domain.settings.SettingsDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class SettingsDataSourceImpl(
    private val settingsDao: SettingsDao,
    private val api: NooliteApi,
    private val parser: NooliteBinParser,
) : SettingsDataSource {
    override suspend fun getGroups(): List<Group> {
        val responseBody = api.getSettings()
        val inputStream = responseBody.byteStream()
        return parser.parse(inputStream.readBytes())
    }

    override fun getAppSettingsFlow(): Flow<AppSettings> {
        return settingsDao.getSettingsFlow().map { entity ->
            entity?.toDomain() ?: AppSettings.default()
        }
    }

    override suspend fun updateAppSettings(settings: AppSettings) {
        settingsDao.insertSettings(settings.toEntity())
    }

    private fun AppSettings.toEntity() = SettingsEntity(
        apiUrl = apiUrl,
        notifyWifiChange = notifyWifiChange,
        darkTheme = theme.name,
    )

    private fun SettingsEntity.toDomain() = AppSettings(
        apiUrl = apiUrl,
        notifyWifiChange = notifyWifiChange,
        theme = AppSettings.Theme.valueOf(darkTheme),
    )
}
