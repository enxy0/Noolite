package com.enxy.noolite.features.model

data class Script(
    val name: String,
    val actionsList: ArrayList<ChannelAction>
) {

    fun write(channelModel: ChannelModel, action: Action, brightness: Int? = null) {
        actionsList.add(ChannelAction(channelModel.id, action, brightness))
    }

    fun write(groupModel: GroupModel, action: Action, brightness: Int? = null) {
        for (channel in groupModel.channelList)
            write(channel, action, brightness)
    }

    fun remove(groupModel: GroupModel, action: Action) {
        for (channel in groupModel.channelList)
            remove(channel, action)
    }

    fun remove(channelModel: ChannelModel, action: Action) {
        for (position in 0..actionsList.size) {
            val channelAction = actionsList[position]
            if (channelAction.channelId == channelModel.id && channelAction.action == action) {
                actionsList.remove(channelAction)
                break
            }
        }
    }
}

enum class Action {
    TURN_ON, TURN_OFF, TOGGLE_STATE, CHANGE_BRIGHTNESS, CHANGE_COLOR, START_OVERFLOW, STOP_OVERFLOW
}

data class ChannelAction(val channelId: Int, val action: Action, val brightness: Int?)