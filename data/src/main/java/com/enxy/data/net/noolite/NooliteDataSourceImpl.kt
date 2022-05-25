package com.enxy.data.net.noolite

import com.enxy.data.net.NetworkDataSource
import com.enxy.domain.features.common.Group
import com.enxy.domain.features.settings.NooliteDataSource

internal class NooliteDataSourceImpl(
    private val api: NooliteApi
) : NooliteDataSource, NetworkDataSource {
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

    override suspend fun getGroups(): List<Group> {
        val responseBody = request { api.getSettings() }
        val inputStream = responseBody.byteStream()
        return NooliteBinParser.parse(inputStream.readBytes())
    }

    override suspend fun toggleLight(channelId: Int) {
        request {
            api.changeLightState(
                channelId = channelId,
                command = TOGGLE_COMMAND
            )
        }
    }

    override suspend fun turnOffLight(channelId: Int) {
        request {
            api.changeLightState(
                channelId = channelId,
                command = TURN_OFF_COMMAND
            )
        }
    }

    override suspend fun turnOnLight(channelId: Int) {
        request {
            api.changeLightState(
                channelId = channelId,
                command = TURN_ON_COMMAND
            )
        }
    }

    override suspend fun changeBrightness(channelId: Int, brightness: Int) {
        request {
            api.changeBacklightState(
                channelId = channelId,
                command = CHANGE_BACKLIGHT_COMMAND,
                fm = FM,
                brightness = brightness
            )
        }
    }

    override suspend fun startOverflow(channelId: Int) {
        request {
            api.changeLightState(
                channelId = channelId,
                command = START_OVERFLOW_COMMAND
            )
        }
    }

    override suspend fun stopOverflow(channelId: Int) {
        request {
            api.changeLightState(
                channelId = channelId,
                command = STOP_OVERFLOW_COMMAND
            )
        }
    }

    override suspend fun changeColor(channelId: Int) {
        request {
            api.changeLightState(
                channelId = channelId,
                command = CHANGE_COLOR_COMMAND
            )
        }
    }
}
