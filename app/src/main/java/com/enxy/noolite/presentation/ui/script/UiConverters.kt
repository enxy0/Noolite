package com.enxy.noolite.presentation.ui.script

import com.enxy.domain.features.common.Channel
import com.enxy.domain.features.common.Group
import com.enxy.noolite.presentation.ui.script.model.ScriptChannel
import com.enxy.noolite.presentation.ui.script.model.ScriptGroup


internal fun Group.toScriptGroup() = ScriptGroup(
    group = this,
    channels = channels.map { channel -> channel.toScriptChannel() }
)

internal fun Channel.toScriptChannel() = ScriptChannel(
    channel = this
)
