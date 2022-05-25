package com.enxy.domain.features.script.model

import com.enxy.domain.features.actions.model.ChannelAction

class CreateScriptPayload(
    val name: String,
    val actions: List<ChannelAction>
)
