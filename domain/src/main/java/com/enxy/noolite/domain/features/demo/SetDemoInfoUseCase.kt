package com.enxy.noolite.domain.features.demo

import com.enxy.noolite.domain.UseCase
import com.enxy.noolite.domain.features.home.HomeDbDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface SetDemoInfoUseCase : UseCase<Unit, Unit>

internal class SetDemoInfoUseCaseImpl(
    private val homeDbDataSource: HomeDbDataSource,
    private val staticInfoDataSource: StaticInfoDataSource
) : SetDemoInfoUseCase {
    override fun execute(param: Unit): Flow<Result<Unit>> = flow {
        val group = staticInfoDataSource.getGroups()
        homeDbDataSource.setGroups(group)
        emit(Result.success(Unit))
    }
}
