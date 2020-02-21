package com.enxy.noolite.features.model

data class Script(
    val name: String,
    val actionsMap: LinkedHashMap<ChannelModel, Action>
)
