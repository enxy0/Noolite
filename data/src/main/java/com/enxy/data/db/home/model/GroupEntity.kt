package com.enxy.data.db.home.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.enxy.data.db.home.ChannelConverters

@Entity
@TypeConverters(ChannelConverters::class)
internal data class GroupEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val channels: List<ChannelEntity>,
    val isFavorite: Boolean
)
