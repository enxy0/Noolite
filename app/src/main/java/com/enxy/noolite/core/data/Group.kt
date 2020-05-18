package com.enxy.noolite.core.data

import java.io.Serializable

data class Group(
    val id: Int = 0,
    val name: String = "empty",
    var channelList: ArrayList<Channel> = ArrayList()
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




