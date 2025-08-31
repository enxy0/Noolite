package com.enxy.noolite.domain.home

import com.enxy.noolite.core.common.asResult
import com.enxy.noolite.core.model.GroupAction
import com.enxy.noolite.core.model.UseCase
import com.enxy.noolite.domain.common.NooliteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface GroupActionUseCase : UseCase<GroupAction, Unit>

internal class GroupActionUseCaseImpl(
    private val dataSource: NooliteDataSource
) : GroupActionUseCase {
    override fun invoke(param: GroupAction): Flow<Result<Unit>> = flow {
        val call: suspend (channelId: Int) -> Unit = { channelId ->
            when (param) {
                is GroupAction.TurnOn -> dataSource.turnOnLight(channelId)
                is GroupAction.TurnOff -> dataSource.turnOffLight(channelId)
            }
        }
        for (channel in param.group.channels) {
            call.invoke(channel.id)
        }
        emit(Unit)
    }.asResult()
}
