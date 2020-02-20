package com.enxy.noolite.features.model

data class Script(
    val name: String,
    val actionsMap: LinkedHashMap<ChannelModel, Action>
)

enum class Action {
    TURN_ON,
    TURN_OFF,
    TOGGLE_STATE,
    CHANGE_BRIGHTNESS,
    CHANGE_COLOR,
    START_OVERFLOW,
    STOP_OVERFLOW,
}
