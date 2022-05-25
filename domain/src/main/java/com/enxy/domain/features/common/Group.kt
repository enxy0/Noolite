package com.enxy.domain.features.common

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
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
