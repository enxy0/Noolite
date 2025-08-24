package com.enxy.noolite.domain.features.home.impl

import com.enxy.noolite.domain.features.home.HomeDbDataSource
import com.enxy.noolite.domain.features.home.SetFavoriteGroupUseCase
import com.enxy.noolite.domain.features.home.model.SetFavoriteGroupPayload
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
