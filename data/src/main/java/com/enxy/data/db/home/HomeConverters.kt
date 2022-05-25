package com.enxy.data.db.home

import androidx.room.TypeConverter
import com.enxy.data.db.home.model.ChannelEntity
import com.enxy.data.db.home.model.GroupEntity
import com.enxy.domain.features.common.Channel
import com.enxy.domain.features.common.Group
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
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
