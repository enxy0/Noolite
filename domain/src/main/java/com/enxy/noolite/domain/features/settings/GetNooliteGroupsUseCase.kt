package com.enxy.noolite.domain.features.settings

import com.enxy.noolite.domain.UseCase
import com.enxy.noolite.domain.asResult
import com.enxy.noolite.domain.common.Group
import com.enxy.noolite.domain.features.home.HomeDbDataSource
import com.enxy.noolite.domain.features.settings.model.NooliteSettingsPayload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface GetNooliteGroupsUseCase : UseCase<NooliteSettingsPayload, List<Group>>

internal class GetNooliteGroupsUseCaseImpl(
    private val nooliteDataSource: NooliteDataSource,
    private val homeDbDataSource: HomeDbDataSource
) : GetNooliteGroupsUseCase {
    override fun invoke(param: NooliteSettingsPayload): Flow<Result<List<Group>>> {
        return flow {
            val groups = nooliteDataSource.getGroups()
            homeDbDataSource.setGroups(groups)
            emit(groups)
        }.asResult()
    }
}
