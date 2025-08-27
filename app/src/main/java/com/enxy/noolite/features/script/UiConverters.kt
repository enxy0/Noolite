package com.enxy.noolite.features.script

import com.enxy.noolite.domain.features.common.Channel
import com.enxy.noolite.domain.features.common.Group
import com.enxy.noolite.features.script.model.ScriptChannel
import com.enxy.noolite.features.script.model.ScriptGroup


internal fun Group.toScriptGroup() = ScriptGroup(
    group = this,
    channels = channels.map { channel -> channel.toScriptChannel() }
)

internal fun Channel.toScriptChannel() = ScriptChannel(
    channel = this
)
