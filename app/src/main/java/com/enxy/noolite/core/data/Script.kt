package com.enxy.noolite.core.data

data class Script(
    var name: String,
    val actionsList: ArrayList<ChannelAction>
) {

    fun write(channel: Channel, action: Action, brightness: Int? = null) {
        // Checking if ChannelAction is in list to prevent adding several actions
        // when user changes brightness with SeekBar
        if (action == Action.CHANGE_BRIGHTNESS) {
            val changeBrightness: ChannelAction? = actionsList.find {
                it.channelId == channel.id && it.action == action
            }
            if (changeBrightness != null) {
                changeBrightness.brightness = brightness
            } else {
                actionsList.add(
                    ChannelAction(
                        channel.id,
                        action,
                        brightness
                    )
                )
            }
        } else {
            actionsList.add(
                ChannelAction(
                    channel.id,
                    action,
                    brightness
                )
            )
        }
    }

    fun write(group: Group, action: Action, brightness: Int? = null) {
        for (channel in group.channelList)
            write(channel, action, brightness)
    }

    fun remove(group: Group, action: Action) {
        for (channel in group.channelList)
            remove(channel, action)
    }

    fun remove(channel: Channel, action: Action) {
        actionsList.removeAll {
            it.channelId == channel.id && it.action == action
        }
    }
}