package com.enxy.noolite.domain.features.settings

import com.enxy.noolite.domain.UseCase
import com.enxy.noolite.domain.asResult
import com.enxy.noolite.domain.features.settings.model.AppSettings
import kotlinx.coroutines.flow.Flow

interface GetAppSettingsUseCase : UseCase<Unit, AppSettings>

internal class GetAppSettingsUseCaseImpl(
    private val dataSource: SettingsDbDataSource
) : GetAppSettingsUseCase {
    override fun invoke(param: Unit): Flow<Result<AppSettings>> {
        return dataSource.getAppSettingsFlow().asResult()
    }
}
