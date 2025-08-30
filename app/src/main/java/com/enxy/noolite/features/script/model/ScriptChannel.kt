package com.enxy.noolite.features.script.model

import com.enxy.noolite.domain.common.Channel
import com.enxy.noolite.domain.features.actions.model.ChannelAction
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
