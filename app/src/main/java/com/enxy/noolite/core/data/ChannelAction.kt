package com.enxy.noolite.core.data

data class ChannelAction(val channelId: Int, val action: Action, var brightness: Int?) {
    companion object {
        fun createTurnOnAction(channelId: Int) =
            ChannelAction(
                channelId,
                Action.TURN_ON,
                null
            )

        fun createTurnOffAction(channelId: Int) =
            ChannelAction(
                channelId,
                Action.TURN_OFF,
                null
            )

        fun createChangeStateAction(channelId: Int) =
            ChannelAction(
                channelId,
                Action.TOGGLE_STATE,
                null
            )

        fun createChangeBrightnessAction(channelId: Int, brightness: Int) =
            ChannelAction(
                channelId,
                Action.CHANGE_BRIGHTNESS,
                brightness
            )

        fun createStartOverflowAction(channelId: Int) =
            ChannelAction(
                channelId,
                Action.START_OVERFLOW,
                null
            )

        fun createStopOverflowAction(channelId: Int) =
            ChannelAction(
                channelId,
                Action.STOP_OVERFLOW,
                null
            )

        fun createChangeColorAction(channelId: Int) =
            ChannelAction(
                channelId,
                Action.CHANGE_COLOR,
                null
            )
    }
}