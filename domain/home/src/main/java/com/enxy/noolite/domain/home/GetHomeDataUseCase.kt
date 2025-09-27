package com.enxy.noolite.domain.home

import com.enxy.noolite.core.common.asResult
import com.enxy.noolite.core.model.UseCase
import com.enxy.noolite.domain.common.HomeDbDataSource
import com.enxy.noolite.domain.common.ScriptDbDataSource
import com.enxy.noolite.domain.home.model.HomeData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

interface GetHomeDataUseCase : UseCase<Unit, HomeData>

internal class GetHomeDataUseCaseImpl(
    private val homeDbDataSource: HomeDbDataSource,
    private val scriptDbDataSource: ScriptDbDataSource,
) : GetHomeDataUseCase {

    override fun invoke(param: Unit): Flow<Result<HomeData>> {
        return combine(
            homeDbDataSource.getGroupsFlow(),
            homeDbDataSource.getFavoriteGroupFlow(),
            scriptDbDataSource.getScripts(),
        ) { groups, favoriteGroup, scripts ->
            HomeData(
                groups = groups,
                favoriteGroup = favoriteGroup,
                scripts = scripts,
            )
        }.asResult()
    }
}
