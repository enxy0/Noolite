package com.enxy.domain.features.home.impl

import com.enxy.domain.features.home.HomeDbDataSource
import com.enxy.domain.features.home.SetFavoriteGroupUseCase
import com.enxy.domain.features.home.model.SetFavoriteGroupPayload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class SetFavoriteGroupUseCaseImpl(
    private val dataSource: HomeDbDataSource
) : SetFavoriteGroupUseCase {
    override fun execute(param: SetFavoriteGroupPayload): Flow<Result<Unit>> = flow {
        dataSource.setFavoriteGroup(param)
        emit(Result.success(Unit))
    }
}
