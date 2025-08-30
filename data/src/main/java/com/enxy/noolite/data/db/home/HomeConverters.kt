package com.enxy.noolite.data.db.home

import androidx.room.TypeConverter
import com.enxy.noolite.data.db.home.model.ChannelEntity
import com.enxy.noolite.data.db.home.model.GroupEntity
import com.enxy.noolite.domain.common.Channel
import com.enxy.noolite.domain.common.Group
import kotlinx.serialization.json.Json

internal class ChannelConverters {
    @TypeConverter
    fun toString(channels: List<ChannelEntity>?): String = Json.encodeToString(channels)

    @TypeConverter
    fun toChannels(json: String?): List<ChannelEntity> = json
        ?.let { Json.decodeFromString<List<ChannelEntity>>(it) }
        .orEmpty()
}

internal fun Group.toEntity() = GroupEntity(
    id = id,
    name = name,
    channels = channels.map { channel -> channel.toEntity() },
    isFavorite = isFavorite
)

internal fun GroupEntity.toDomain() = Group(
    id = id,
    name = name,
    channels = channels.map { channel -> channel.toDomain() },
    isFavorite = isFavorite
)

internal fun Channel.toEntity() = ChannelEntity(
    id = id,
    name = name,
    type = type
)

internal fun ChannelEntity.toDomain() = Channel(
    id = id,
    name = name,
    type = type
)
