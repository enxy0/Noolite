package com.enxy.data.db.script.model

import kotlinx.serialization.Serializable

@Serializable
data class ChannelActionEntity(
    val channelId: Int,
    val brightness: Int? = null,
    val action: ChannelActionTypeEntity
)
