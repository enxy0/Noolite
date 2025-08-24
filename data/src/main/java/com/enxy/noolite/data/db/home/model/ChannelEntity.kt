package com.enxy.noolite.data.db.home.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
internal data class ChannelEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val type: Int
)
