package com.enxy.noolite.feature.script.model

import com.enxy.noolite.core.model.Channel
import com.enxy.noolite.core.model.ChannelAction
import kotlinx.serialization.Serializable

@Serializable
data class ScriptChannel(
    val channel: Channel,
    val actions: Set<ChannelAction> = emptySet(),
    val expanded: Boolean = false,
) {
    val id: Int get() = channel.id

    inline fun <reified T : ChannelAction> contains(): Boolean =
        actions.firstNotNullOfOrNull { action -> action as? T } != null
}

fun Channel.toScriptChannel() = ScriptChannel(
    channel = this
)
