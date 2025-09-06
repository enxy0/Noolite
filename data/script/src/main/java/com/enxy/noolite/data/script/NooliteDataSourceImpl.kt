package com.enxy.noolite.data.script

import com.enxy.noolite.core.network.api.NooliteApi
import com.enxy.noolite.domain.common.NooliteDataSource

internal class NooliteDataSourceImpl(
    private val api: NooliteApi,
) : NooliteDataSource {
    private companion object {
        private const val TURN_OFF_COMMAND = 0
        private const val TURN_ON_COMMAND = 2
        private const val TOGGLE_COMMAND = 4
        private const val FM = 3
        private const val CHANGE_BACKLIGHT_COMMAND = 6
        private const val CHANGE_COLOR_COMMAND = 17
        private const val START_OVERFLOW_COMMAND = 16
        private const val STOP_OVERFLOW_COMMAND = 10
    }

    override suspend fun toggleLight(channelId: Int) {
        api.changeLightState(
            channelId = channelId,
            command = TOGGLE_COMMAND
        )
    }

    override suspend fun turnOffLight(channelId: Int) {
        api.changeLightState(
            channelId = channelId,
            command = TURN_OFF_COMMAND
        )
    }

    override suspend fun turnOnLight(channelId: Int) {
        api.changeLightState(
            channelId = channelId,
            command = TURN_ON_COMMAND
        )
    }

    override suspend fun changeBrightness(channelId: Int, brightness: Int) {
        api.changeBacklightState(
            channelId = channelId,
            command = CHANGE_BACKLIGHT_COMMAND,
            fm = FM,
            brightness = brightness
        )
    }

    override suspend fun startOverflow(channelId: Int) {
        api.changeLightState(
            channelId = channelId,
            command = START_OVERFLOW_COMMAND
        )
    }

    override suspend fun stopOverflow(channelId: Int) {
        api.changeLightState(
            channelId = channelId,
            command = STOP_OVERFLOW_COMMAND
        )
    }

    override suspend fun changeColor(channelId: Int) {
        api.changeLightState(
            channelId = channelId,
            command = CHANGE_COLOR_COMMAND
        )
    }
}
