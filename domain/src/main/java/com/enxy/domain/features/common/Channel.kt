package com.enxy.domain.features.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Channel(
    val id: Int = 0,
    val name: String = "empty",
    val type: Int = 0
) : Parcelable {

    private companion object {
        const val CHANNEL_TYPE_DEFAULT = 0
        const val CHANNEL_TYPE_WITH_BACKLIGHT = 1
        const val CHANNEL_TYPE_WITH_BACKLIGHT_AND_COLOR = 3
    }

    val hasLight: Boolean
        get() = type in setOf(
            CHANNEL_TYPE_DEFAULT,
            CHANNEL_TYPE_WITH_BACKLIGHT,
            CHANNEL_TYPE_WITH_BACKLIGHT_AND_COLOR
        )

    val hasBrightness: Boolean
        get() = type in setOf(
            CHANNEL_TYPE_WITH_BACKLIGHT,
            CHANNEL_TYPE_WITH_BACKLIGHT_AND_COLOR
        )

    val hasBacklight: Boolean
        get() = type == CHANNEL_TYPE_WITH_BACKLIGHT_AND_COLOR
}
