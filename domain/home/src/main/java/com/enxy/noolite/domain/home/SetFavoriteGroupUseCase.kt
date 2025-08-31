package com.enxy.noolite.domain.home

import com.enxy.noolite.core.common.asResult
import com.enxy.noolite.core.model.UseCase
import com.enxy.noolite.domain.common.HomeDbDataSource
import com.enxy.noolite.domain.common.model.SetFavoriteGroupPayload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface SetFavoriteGroupUseCase : UseCase<SetFavoriteGroupPayload, Unit>

internal class SetFavoriteGroupUseCaseImpl(
    private val dataSource: HomeDbDataSource
) : SetFavoriteGroupUseCase {
    override fun invoke(param: SetFavoriteGroupPayload): Flow<Result<Unit>> = flow {
        emit(dataSource.setFavoriteGroup(param))
    }.asResult()
}
