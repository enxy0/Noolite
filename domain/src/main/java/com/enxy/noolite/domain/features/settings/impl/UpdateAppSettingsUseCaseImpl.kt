package com.enxy.noolite.domain.features.settings.impl

import com.enxy.noolite.domain.features.settings.SettingsDbDataSource
import com.enxy.noolite.domain.features.settings.UpdateAppSettingsUseCase
import com.enxy.noolite.domain.features.settings.model.AppSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class UpdateAppSettingsUseCaseImpl(
    private val settingsDbDataSource: SettingsDbDataSource
) : UpdateAppSettingsUseCase {
    override fun execute(param: AppSettings): Flow<Result<Unit>> = flow {
        settingsDbDataSource.updateAppSettings(param)
        emit(Result.success(Unit))
    }
}
