package com.enxy.noolite.core.model

data class Script(
    val id: Int,
    val name: String,
    val actions: List<ChannelAction>
)
