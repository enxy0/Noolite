package com.enxy.noolite.domain.features.home

import com.enxy.noolite.domain.UseCase
import com.enxy.noolite.domain.asResult
import com.enxy.noolite.domain.features.home.model.SetFavoriteGroupPayload
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
