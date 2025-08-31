package com.enxy.noolite.domain.home

import com.enxy.noolite.core.common.asResult
import com.enxy.noolite.core.model.Group
import com.enxy.noolite.core.model.UseCase
import com.enxy.noolite.domain.common.HomeDbDataSource
import kotlinx.coroutines.flow.Flow

interface GetFavoriteGroupUseCase : UseCase<Unit, Group?>

internal class GetFavoriteGroupUseCaseImpl(
    private val dataSource: HomeDbDataSource
) : GetFavoriteGroupUseCase {
    override fun invoke(param: Unit): Flow<Result<Group?>> {
        return dataSource.getFavoriteGroupFlow().asResult()
    }
}
