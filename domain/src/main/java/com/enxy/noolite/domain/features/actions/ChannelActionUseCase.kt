package com.enxy.noolite.domain.features.actions

import com.enxy.noolite.domain.UseCase
import com.enxy.noolite.domain.asResult
import com.enxy.noolite.domain.features.actions.model.ChannelAction
import com.enxy.noolite.domain.features.settings.NooliteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface ChannelActionUseCase : UseCase<ChannelAction, Unit>

internal class ChannelActionUseCaseImpl(
    private val dataSource: NooliteDataSource
) : ChannelActionUseCase {
    override fun invoke(param: ChannelAction): Flow<Result<Unit>> = flow {
        when (param) {
            is ChannelAction.TurnOff -> {
                dataSource.turnOffLight(param.channelId)
            }
            is ChannelAction.TurnOn -> {
                dataSource.turnOnLight(param.channelId)
            }
            is ChannelAction.Toggle -> {
                dataSource.toggleLight(param.channelId)
            }
            is ChannelAction.ChangeBrightness -> {
                dataSource.changeBrightness(param.channelId, param.brightness)
            }
            is ChannelAction.ChangeColor -> {
                dataSource.changeColor(param.channelId)
            }
            is ChannelAction.StartOverflow -> {
                dataSource.startOverflow(param.channelId)
            }
            is ChannelAction.StopOverflow -> {
                dataSource.stopOverflow(param.channelId)
            }
        }
        emit(Unit)
    }.asResult()
}
