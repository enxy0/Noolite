package com.enxy.domain.features.common

import com.enxy.domain.features.actions.model.ChannelAction

data class Script(
    val id: Int,
    val name: String,
    val actions: List<ChannelAction>
)
