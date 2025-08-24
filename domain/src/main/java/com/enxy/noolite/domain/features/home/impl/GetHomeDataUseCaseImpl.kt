package com.enxy.noolite.domain.features.home.impl

import com.enxy.noolite.domain.features.home.GetHomeDataUseCase
import com.enxy.noolite.domain.features.home.HomeDbDataSource
import com.enxy.noolite.domain.features.home.model.HomeData
import com.enxy.noolite.domain.features.script.ScriptDbDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

internal class GetHomeDataUseCaseImpl(
    private val homeDbDataSource: HomeDbDataSource,
    private val scriptDbDataSource: ScriptDbDataSource
) : GetHomeDataUseCase {

    override fun execute(param: Unit): Flow<Result<HomeData>> = combine(
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
