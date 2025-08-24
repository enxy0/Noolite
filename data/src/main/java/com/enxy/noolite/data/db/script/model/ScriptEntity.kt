package com.enxy.noolite.data.db.script.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.enxy.noolite.data.db.script.ScriptConverters

@Entity(tableName = "Script")
@TypeConverters(ScriptConverters::class)
data class ScriptEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val actions: List<ChannelActionEntity>
)
