package com.enxy.noolite.features.model

data class Script(
    var name: String,
    val actionsList: ArrayList<ChannelAction>
) {

    fun write(channel: Channel, action: Action, brightness: Int? = null) {
        actionsList.add(ChannelAction(channel.id, action, brightness))
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
        for (position in 0 until actionsList.size) {
            val channelAction = actionsList[position]
            if (channelAction.channelId == channel.id && channelAction.action == action) {
                actionsList.remove(channelAction)
                break
            }
        }
    }
}