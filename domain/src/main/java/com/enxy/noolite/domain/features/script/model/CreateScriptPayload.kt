package com.enxy.noolite.domain.features.script.model

import com.enxy.noolite.domain.features.actions.model.ChannelAction

class CreateScriptPayload(
    val name: String,
    val actions: List<ChannelAction>
)
