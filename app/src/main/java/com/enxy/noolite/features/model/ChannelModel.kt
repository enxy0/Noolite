package com.enxy.noolite.features.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class ChannelModel(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("name") val name: String = "empty",
    @SerializedName("type") val type: Int = 0
) : Serializable