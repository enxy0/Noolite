package com.enxy.noolite.presentation.ui.script.model

import com.enxy.noolite.domain.features.actions.model.ChannelAction
import com.enxy.noolite.domain.features.actions.model.GroupAction
import com.enxy.noolite.domain.features.common.Group

data class ScriptGroup(
    val group: Group,
    val channels: List<ScriptChannel>,
    val actions: List<GroupAction> = emptyList()
) {
    inline fun <reified T : GroupAction> contains(): Boolean =
        actions.firstNotNullOfOrNull { action -> action as? T } != null

    fun toChannelActions(): List<ChannelAction> = buildList {
        for (action in actions) {
            addAll(ChannelAction.from(action))
        }
        for (channel in channels) {
            addAll(channel.actions)
        }
    }
}
