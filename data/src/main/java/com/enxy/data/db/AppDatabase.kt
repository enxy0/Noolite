package com.enxy.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.enxy.data.db.home.HomeDao
import com.enxy.data.db.home.model.ChannelEntity
import com.enxy.data.db.home.model.GroupEntity
import com.enxy.data.db.script.ScriptDao
import com.enxy.data.db.script.model.ScriptEntity
import com.enxy.data.db.settings.SettingsDao
import com.enxy.data.db.settings.model.SettingsEntity

@Database(
    entities = [
        SettingsEntity::class,
        GroupEntity::class,
        ChannelEntity::class,
        ScriptEntity::class
    ],
    version = 1
)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun settingsDao(): SettingsDao
    abstract fun homeDao(): HomeDao
    abstract fun scriptDao(): ScriptDao
}
