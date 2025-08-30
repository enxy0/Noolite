package com.enxy.noolite.data.db.script

import androidx.room.TypeConverter
import com.enxy.noolite.data.db.script.model.ChannelActionEntity
import com.enxy.noolite.data.db.script.model.ChannelActionTypeEntity
import com.enxy.noolite.data.db.script.model.ScriptEntity
import com.enxy.noolite.domain.common.Script
import com.enxy.noolite.domain.features.actions.model.ChannelAction
import kotlinx.serialization.json.Json

class ScriptConverters {
    @TypeConverter
    fun toString(channels: List<ChannelActionEntity>?): String = Json.encodeToString(channels)

    @TypeConverter
    fun toChannelActions(json: String?): List<ChannelActionEntity> = json
        ?.let { Json.decodeFromString<List<ChannelActionEntity>>(it) }
        .orEmpty()
}

internal fun Script.toEntity() = ScriptEntity(
    id = id,
    name = name,
    actions = actions.map { action -> action.toEntity() }
)

internal fun ScriptEntity.toDomain() = Script(
    id = id,
    name = name,
    actions = actions.map { action -> action.toDomain() }
)

internal fun ChannelAction.toEntity() = ChannelActionEntity(
    channelId = channelId,
    brightness = (this as? ChannelAction.ChangeBrightness)?.brightness,
    action = when (this) {
        is ChannelAction.TurnOn -> ChannelActionTypeEntity.TURN_ON
        is ChannelAction.TurnOff -> ChannelActionTypeEntity.TURN_OFF
        is ChannelAction.Toggle -> ChannelActionTypeEntity.TOGGLE
        is ChannelAction.ChangeBrightness -> ChannelActionTypeEntity.CHANGE_BRIGHTNESS
        is ChannelAction.ChangeColor -> ChannelActionTypeEntity.CHANGE_COLOR
        is ChannelAction.StartOverflow -> ChannelActionTypeEntity.START_OVERFLOW
        is ChannelAction.StopOverflow -> ChannelActionTypeEntity.STOP_OVERFLOW
    }
)

internal fun ChannelActionEntity.toDomain() = when (action) {
    ChannelActionTypeEntity.TURN_ON -> ChannelAction.TurnOn(channelId)
    ChannelActionTypeEntity.TURN_OFF -> ChannelAction.TurnOff(channelId)
    ChannelActionTypeEntity.TOGGLE -> ChannelAction.Toggle(channelId)
    ChannelActionTypeEntity.CHANGE_BRIGHTNESS -> ChannelAction.ChangeBrightness(
        channelId,
        brightness!!
    )
    ChannelActionTypeEntity.CHANGE_COLOR -> ChannelAction.ChangeColor(channelId)
    ChannelActionTypeEntity.START_OVERFLOW -> ChannelAction.StartOverflow(channelId)
    ChannelActionTypeEntity.STOP_OVERFLOW -> ChannelAction.StopOverflow(channelId)
}
