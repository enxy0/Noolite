package com.enxy.noolite.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Group(
    val id: Int = 0,
    val name: String = "",
    val channels: List<Channel> = emptyList(),
    val isFavorite: Boolean = false
) {

    val channelNames: String
        get() = channels
            .filter { channel -> channel.name.isNotBlank() }
            .joinToString(prefix = "", postfix = "") { channel -> channel.name }
}
