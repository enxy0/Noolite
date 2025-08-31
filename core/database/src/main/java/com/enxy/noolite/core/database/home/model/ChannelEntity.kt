package com.enxy.noolite.core.database.home.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class ChannelEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val type: Int
)
