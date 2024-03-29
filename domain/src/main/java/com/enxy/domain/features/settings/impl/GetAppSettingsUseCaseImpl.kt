package com.enxy.domain.features.settings.impl

import com.enxy.domain.features.settings.GetAppSettingsUseCase
import com.enxy.domain.features.settings.SettingsDbDataSource
import com.enxy.domain.features.settings.model.AppSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class GetAppSettingsUseCaseImpl(
    private val dataSource: SettingsDbDataSource
) : GetAppSettingsUseCase {
    override fun execute(param: Unit): Flow<Result<AppSettings>> = dataSource
        .getAppSettingsFlow()
        .map { settings -> Result.success(settings) }
}
