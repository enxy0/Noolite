package com.enxy.noolite.domain.common.model

import com.enxy.noolite.core.model.ChannelAction

class CreateScriptPayload(
    val name: String,
    val actions: List<ChannelAction>
)
