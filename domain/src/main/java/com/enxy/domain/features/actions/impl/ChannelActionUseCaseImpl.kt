package com.enxy.domain.features.actions.impl

import com.enxy.domain.features.actions.ChannelActionUseCase
import com.enxy.domain.features.actions.model.ChannelAction
import com.enxy.domain.features.settings.NooliteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class ChannelActionUseCaseImpl(
    private val dataSource: NooliteDataSource
) : ChannelActionUseCase {
    override fun execute(param: ChannelAction): Flow<Result<Unit>> = flow {
        when (param) {
            is ChannelAction.TurnOff -> dataSource.turnOffLight(param.channelId)
            is ChannelAction.TurnOn -> dataSource.turnOnLight(param.channelId)
            is ChannelAction.Toggle -> dataSource.toggleLight(param.channelId)
            is ChannelAction.ChangeBrightness -> dataSource.changeBrightness(
                param.channelId,
                param.brightness
            )
            is ChannelAction.ChangeColor -> dataSource.changeColor(param.channelId)
            is ChannelAction.StartOverflow -> dataSource.startOverflow(param.channelId)
            is ChannelAction.StopOverflow -> dataSource.stopOverflow(param.channelId)
        }
        emit(Result.success(Unit))
    }
}
