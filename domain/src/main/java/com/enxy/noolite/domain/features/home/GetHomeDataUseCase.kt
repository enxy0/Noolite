package com.enxy.noolite.domain.features.home

import com.enxy.noolite.domain.UseCase
import com.enxy.noolite.domain.features.home.model.HomeData
import com.enxy.noolite.domain.features.script.ScriptDbDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

interface GetHomeDataUseCase : UseCase<Unit, HomeData>

internal class GetHomeDataUseCaseImpl(
    private val homeDbDataSource: HomeDbDataSource,
    private val scriptDbDataSource: ScriptDbDataSource
) : GetHomeDataUseCase {

    override fun invoke(param: Unit): Flow<Result<HomeData>> {
        return combine(
            homeDbDataSource.getGroupsFlow(),
            homeDbDataSource.getFavoriteGroupFlow(),
            scriptDbDataSource.getScripts()
        ) { groups, favoriteGroup, scripts ->
            val data = HomeData(
                groups = groups,
                favoriteGroup = favoriteGroup,
                scripts = scripts
            )
            Result.success(data)
        }
    }
}
