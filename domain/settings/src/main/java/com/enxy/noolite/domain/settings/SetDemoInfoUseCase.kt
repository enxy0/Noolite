package com.enxy.noolite.domain.settings

import com.enxy.noolite.core.common.asResult
import com.enxy.noolite.core.model.UseCase
import com.enxy.noolite.domain.common.HomeDbDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface SetDemoInfoUseCase : UseCase<Unit, Unit>

internal class SetDemoInfoUseCaseImpl(
    private val homeDbDataSource: HomeDbDataSource,
    private val staticInfoDataSource: StaticInfoDataSource
) : SetDemoInfoUseCase {
    override fun invoke(param: Unit): Flow<Result<Unit>> = flow {
        val group = staticInfoDataSource.getGroups()
        emit(homeDbDataSource.setGroups(group))
    }.asResult()
}
