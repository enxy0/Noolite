package com.enxy.noolite.domain.features.actions.model

import kotlinx.serialization.Serializable

@Serializable
sealed interface ChannelAction {
    companion object {
        fun from(action: GroupAction): List<ChannelAction> = when (action) {
            is GroupAction.TurnOn -> action.group.channels.map { channel -> TurnOn(channel.id) }
            is GroupAction.TurnOff -> action.group.channels.map { channel -> TurnOff(channel.id) }
        }
    }

    val channelId: Int

    @Serializable
    data class TurnOn(override val channelId: Int) : ChannelAction

    @Serializable
    data class TurnOff(override val channelId: Int) : ChannelAction

    @Serializable
    data class Toggle(override val channelId: Int) : ChannelAction

    @Serializable
    data class ChangeBrightness(override val channelId: Int, val brightness: Int) : ChannelAction {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as ChangeBrightness

            return channelId == other.channelId
        }

        override fun hashCode(): Int {
            return channelId
        }
    }

    @Serializable
    data class ChangeColor(override val channelId: Int) : ChannelAction

    @Serializable
    data class StartOverflow(override val channelId: Int) : ChannelAction

    @Serializable
    data class StopOverflow(override val channelId: Int) : ChannelAction
}
