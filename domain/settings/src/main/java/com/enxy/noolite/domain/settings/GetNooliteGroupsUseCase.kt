package com.enxy.noolite.domain.settings

import com.enxy.noolite.core.common.asResult
import com.enxy.noolite.core.model.Group
import com.enxy.noolite.core.model.UseCase
import com.enxy.noolite.domain.common.HomeDbDataSource
import com.enxy.noolite.domain.settings.model.NooliteSettingsPayload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

interface GetNooliteGroupsUseCase : UseCase<NooliteSettingsPayload, List<Group>>

internal class GetNooliteGroupsUseCaseImpl(
    private val settingsDataSource: SettingsDataSource,
    private val homeDbDataSource: HomeDbDataSource,
) : GetNooliteGroupsUseCase {
    override fun invoke(param: NooliteSettingsPayload): Flow<Result<List<Group>>> {
        return flow {
            val settings = settingsDataSource.getAppSettingsFlow().first()
            settingsDataSource.updateAppSettings(settings.copy(apiUrl = param.apiUrl))
            val groups = settingsDataSource.getGroups()
            homeDbDataSource.setGroups(groups)
            emit(groups)
        }.asResult()
    }
}
