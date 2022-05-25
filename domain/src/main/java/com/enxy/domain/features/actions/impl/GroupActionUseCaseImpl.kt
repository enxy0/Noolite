package com.enxy.domain.features.actions.impl

import com.enxy.domain.features.actions.GroupActionUseCase
import com.enxy.domain.features.actions.model.GroupAction
import com.enxy.domain.features.settings.NooliteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class GroupActionUseCaseImpl(
    private val dataSource: NooliteDataSource
) : GroupActionUseCase {
    override fun execute(param: GroupAction): Flow<Result<Unit>> = flow {
        val call: suspend (channelId: Int) -> Unit = { channelId ->
            when (param) {
                is GroupAction.TurnOn -> dataSource.turnOnLight(channelId)
                is GroupAction.TurnOff -> dataSource.turnOffLight(channelId)
            }
        }
        for (channel in param.group.channels) {
            call.invoke(channel.id)
        }
        emit(Result.success(Unit))
    }
}
