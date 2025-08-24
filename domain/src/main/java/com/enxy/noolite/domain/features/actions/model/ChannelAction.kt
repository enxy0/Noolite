package com.enxy.noolite.domain.features.actions.model

sealed class ChannelAction(open val channelId: Int) {
    companion object {
        fun from(action: GroupAction): List<ChannelAction> = when (action) {
            is GroupAction.TurnOn -> action.group.channels.map { channel -> TurnOn(channel.id) }
            is GroupAction.TurnOff -> action.group.channels.map { channel -> TurnOff(channel.id) }
        }
    }

    data class TurnOn(override val channelId: Int) : ChannelAction(channelId)
    data class TurnOff(override val channelId: Int) : ChannelAction(channelId)
    data class Toggle(override val channelId: Int) : ChannelAction(channelId)
    data class ChangeBrightness(override val channelId: Int, val brightness: Int) :
        ChannelAction(channelId)

    data class ChangeColor(override val channelId: Int) : ChannelAction(channelId)
    data class StartOverflow(override val channelId: Int) : ChannelAction(channelId)
    data class StopOverflow(override val channelId: Int) : ChannelAction(channelId)
}
