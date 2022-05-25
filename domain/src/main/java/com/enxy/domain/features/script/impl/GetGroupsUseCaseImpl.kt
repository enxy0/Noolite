package com.enxy.domain.features.script.impl

import com.enxy.domain.features.common.Group
import com.enxy.domain.features.home.HomeDbDataSource
import com.enxy.domain.features.script.GetGroupsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class GetGroupsUseCaseImpl(
    private val homeDbDataSource: HomeDbDataSource
) : GetGroupsUseCase {
    override fun execute(param: Unit): Flow<Result<List<Group>>> {
        return homeDbDataSource.getGroupsFlow().map { group -> Result.success(group) }
    }
}
