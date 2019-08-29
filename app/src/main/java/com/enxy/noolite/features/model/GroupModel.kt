package com.enxy.noolite.features.model

import com.google.gson.annotations.SerializedName


data class GroupModel(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("name") val name: String = "empty",
    @SerializedName("channelElementsList") var channelModelList: ArrayList<ChannelModel> = ArrayList()
) {

    fun channelElementsToString(): String {
        return if (channelModelList.isNotEmpty()) {
            var result = ""
            for (i in 0 until channelModelList.size - 2) {
                result += "${channelModelList[i].name}, "
            }
            result += "${channelModelList[(channelModelList.size - 2)].name}, ${channelModelList[(channelModelList.size - 1)].name}"
            result
        } else
            "empty"
    }
}




