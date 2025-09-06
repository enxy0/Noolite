package com.enxy.noolite.domain.settings

import com.enxy.noolite.core.common.asResult
import com.enxy.noolite.core.model.AppSettings
import com.enxy.noolite.core.model.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface UpdateAppSettingsUseCase : UseCase<AppSettings, Unit>

internal class UpdateAppSettingsUseCaseImpl(
    private val settingsDataSource: SettingsDataSource
) : UpdateAppSettingsUseCase {
    override fun invoke(param: AppSettings): Flow<Result<Unit>> = flow {
        emit(settingsDataSource.updateAppSettings(param))
    }.asResult()
}
