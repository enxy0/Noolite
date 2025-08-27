package com.enxy.noolite.features.script.model

import com.enxy.noolite.domain.features.actions.model.ChannelAction
import com.enxy.noolite.domain.features.actions.model.GroupAction
import com.enxy.noolite.domain.features.common.Group
import kotlinx.serialization.Serializable

@Serializable
data class ScriptGroup(
    val group: Group,
    val channels: List<ScriptChannel>,
    val actions: Set<GroupAction> = emptySet(),
    val expanded: Boolean = false,
) {
    inline fun <reified T : GroupAction> contains(): Boolean =
        actions.firstNotNullOfOrNull { action -> action as? T } != null

    fun toChannelActions(): Set<ChannelAction> = buildSet {
        for (action in actions) {
            addAll(ChannelAction.from(action))
        }
        for (channel in channels) {
            addAll(channel.actions)
        }
    }
}
