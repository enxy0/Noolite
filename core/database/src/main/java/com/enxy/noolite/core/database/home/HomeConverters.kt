package com.enxy.noolite.core.database.home

import androidx.room.TypeConverter
import com.enxy.noolite.core.database.home.model.ChannelEntity
import com.enxy.noolite.core.database.home.model.GroupEntity
import com.enxy.noolite.core.model.Channel
import com.enxy.noolite.core.model.Group
import kotlinx.serialization.json.Json

internal class ChannelConverters {
    @TypeConverter
    fun toString(channels: List<ChannelEntity>?): String = Json.encodeToString(channels)

    @TypeConverter
    fun toChannels(json: String?): List<ChannelEntity> = json
        ?.let { Json.decodeFromString<List<ChannelEntity>>(it) }
        .orEmpty()
}

fun Group.toEntity() = GroupEntity(
    id = id,
    name = name,
    channels = channels.map { channel -> channel.toEntity() },
    isFavorite = isFavorite
)

fun GroupEntity.toDomain() = Group(
    id = id,
    name = name,
    channels = channels.map { channel -> channel.toDomain() },
    isFavorite = isFavorite
)

fun Channel.toEntity() = ChannelEntity(
    id = id,
    name = name,
    type = type
)

fun ChannelEntity.toDomain() = Channel(
    id = id,
    name = name,
    type = type
)
