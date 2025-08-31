package com.enxy.noolite.domain.script

import com.enxy.noolite.core.common.asResult
import com.enxy.noolite.core.model.Group
import com.enxy.noolite.core.model.UseCase
import com.enxy.noolite.domain.common.HomeDbDataSource
import kotlinx.coroutines.flow.Flow

interface GetGroupsUseCase : UseCase<Unit, List<Group>>

internal class GetGroupsUseCaseImpl(
    private val homeDbDataSource: HomeDbDataSource
) : GetGroupsUseCase {
    override fun invoke(param: Unit): Flow<Result<List<Group>>> {
        return homeDbDataSource.getGroupsFlow().asResult()
    }
}
