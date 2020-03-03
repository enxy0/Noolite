package com.enxy.noolite.features.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Group(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("name") val name: String = "empty",
    @SerializedName("channelElementsList") var channelList: ArrayList<Channel> = ArrayList()
) : Serializable {

    fun channelElementsToString(): String {
        return if (channelList.isNotEmpty()) {
            var result = ""
            for (i in 0 until channelList.size - 2) {
                result += "${channelList[i].name}, "
            }
            result += "${channelList[(channelList.size - 2)].name}, ${channelList[(channelList.size - 1)].name}"
            result
        } else
            "empty"
    }
}




