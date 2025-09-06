package com.enxy.noolite.domain.settings

import com.enxy.noolite.core.common.asResult
import com.enxy.noolite.core.model.AppSettings
import com.enxy.noolite.domain.common.GetAppSettingsUseCase
import kotlinx.coroutines.flow.Flow

internal class GetAppSettingsUseCaseImpl(
    private val dataSource: SettingsDataSource
) : GetAppSettingsUseCase {
    override fun invoke(param: Unit): Flow<Result<AppSettings>> {
        return dataSource.getAppSettingsFlow().asResult()
    }
}
