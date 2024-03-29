package com.enxy.domain.features.home.impl

import com.enxy.domain.features.common.Group
import com.enxy.domain.features.home.GetFavoriteGroupUseCase
import com.enxy.domain.features.home.HomeDbDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class GetFavoriteGroupUseCaseImpl(
    private val dataSource: HomeDbDataSource
) : GetFavoriteGroupUseCase {
    override fun execute(param: Unit): Flow<Result<Group?>> =
        dataSource.getFavoriteGroupFlow().map { group -> Result.success(group) }
}
