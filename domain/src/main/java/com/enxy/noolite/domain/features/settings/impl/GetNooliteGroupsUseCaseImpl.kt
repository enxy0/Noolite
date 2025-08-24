package com.enxy.noolite.domain.features.settings.impl

import com.enxy.noolite.domain.features.common.Group
import com.enxy.noolite.domain.features.home.HomeDbDataSource
import com.enxy.noolite.domain.features.settings.GetNooliteGroupsUseCase
import com.enxy.noolite.domain.features.settings.NooliteDataSource
import com.enxy.noolite.domain.features.settings.model.NooliteSettingsPayload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class GetNooliteGroupsUseCaseImpl(
    private val nooliteDataSource: NooliteDataSource,
    private val homeDbDataSource: HomeDbDataSource
) : GetNooliteGroupsUseCase {
    override fun execute(param: NooliteSettingsPayload): Flow<Result<List<Group>>> = flow {
        val groups = nooliteDataSource.getGroups()
        homeDbDataSource.setGroups(groups)
        emit(Result.success(groups))
    }
}
