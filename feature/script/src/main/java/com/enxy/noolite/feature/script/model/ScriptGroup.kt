package com.enxy.noolite.feature.script.model

import androidx.compose.runtime.Immutable
import com.enxy.noolite.core.model.ChannelAction
import com.enxy.noolite.core.model.Group
import com.enxy.noolite.core.model.GroupAction
import kotlinx.serialization.Serializable

@Immutable
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

fun Group.toScriptGroup() = ScriptGroup(
    group = this,
    channels = channels.map { channel -> channel.toScriptChannel() }
)

