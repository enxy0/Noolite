package com.enxy.noolite.core.database.home.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.enxy.noolite.core.database.home.ChannelConverters

@Entity
@TypeConverters(ChannelConverters::class)
data class GroupEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val channels: List<ChannelEntity>,
    val isFavorite: Boolean
)
