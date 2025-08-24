package com.enxy.noolite.domain.features.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Group(
    val id: Int = 0,
    val name: String = "",
    val channels: List<Channel> = emptyList(),
    val isFavorite: Boolean = false
) : Parcelable {

    val channelNames: String
        get() = channels
            .filter { channel -> channel.name.isNotBlank() }
            .joinToString(prefix = "", postfix = "") { channel -> channel.name }
}
