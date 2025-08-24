package com.enxy.noolite.presentation.ui.script.model

import com.enxy.noolite.domain.features.actions.model.ChannelAction
import com.enxy.noolite.domain.features.common.Channel

data class ScriptChannel(
    val channel: Channel,
    val actions: List<ChannelAction> = emptyList()
) {
    val id: Int get() = channel.id

    inline fun <reified T : ChannelAction> contains(): Boolean =
        actions.firstNotNullOfOrNull { action -> action as? T } != null
}
