package com.enxy.noolite.features.model

import com.google.gson.annotations.SerializedName

data class GroupListHolderModel(@SerializedName("groupElementsList") val groupListModel: ArrayList<GroupModel>)