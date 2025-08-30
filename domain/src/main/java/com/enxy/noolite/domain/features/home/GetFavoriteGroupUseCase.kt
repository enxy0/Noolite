package com.enxy.noolite.domain.features.home

import com.enxy.noolite.domain.UseCase
import com.enxy.noolite.domain.asResult
import com.enxy.noolite.domain.common.Group
import kotlinx.coroutines.flow.Flow

interface GetFavoriteGroupUseCase : UseCase<Unit, Group?>

internal class GetFavoriteGroupUseCaseImpl(
    private val dataSource: HomeDbDataSource
) : GetFavoriteGroupUseCase {
    override fun invoke(param: Unit): Flow<Result<Group?>> {
        return dataSource.getFavoriteGroupFlow().asResult()
    }
}
