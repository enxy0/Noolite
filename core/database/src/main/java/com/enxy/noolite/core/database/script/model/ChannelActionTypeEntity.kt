package com.enxy.noolite.core.database.script.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
enum class ChannelActionTypeEntity(val value: String) {
    TURN_ON("turn_on"),
    TURN_OFF("turn_off"),
    TOGGLE("toggle"),
    CHANGE_BRIGHTNESS("change_brightness"),
    CHANGE_COLOR("change_color"),
    START_OVERFLOW("start_overflow"),
    STOP_OVERFLOW("stop_overflow");
}
