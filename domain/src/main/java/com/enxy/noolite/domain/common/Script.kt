package com.enxy.noolite.domain.common

import com.enxy.noolite.domain.features.actions.model.ChannelAction

data class Script(
    val id: Int,
    val name: String,
    val actions: List<ChannelAction>
)
