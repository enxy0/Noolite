package com.enxy.noolite.domain.features.settings

import com.enxy.noolite.domain.UseCase
import com.enxy.noolite.domain.asResult
import com.enxy.noolite.domain.features.settings.model.AppSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface UpdateAppSettingsUseCase : UseCase<AppSettings, Unit>

internal class UpdateAppSettingsUseCaseImpl(
    private val settingsDbDataSource: SettingsDbDataSource
) : UpdateAppSettingsUseCase {
    override fun invoke(param: AppSettings): Flow<Result<Unit>> = flow {
        emit(settingsDbDataSource.updateAppSettings(param))
    }.asResult()
}
