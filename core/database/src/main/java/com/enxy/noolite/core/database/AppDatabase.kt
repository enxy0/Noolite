package com.enxy.noolite.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.enxy.noolite.core.database.home.HomeDao
import com.enxy.noolite.core.database.home.model.ChannelEntity
import com.enxy.noolite.core.database.home.model.GroupEntity
import com.enxy.noolite.core.database.script.ScriptDao
import com.enxy.noolite.core.database.script.model.ScriptEntity
import com.enxy.noolite.core.database.settings.SettingsDao
import com.enxy.noolite.core.database.settings.model.SettingsEntity

@Database(
    entities = [
        SettingsEntity::class,
        GroupEntity::class,
        ChannelEntity::class,
        ScriptEntity::class
    ],
    version = 1,
)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun settingsDao(): SettingsDao
    abstract fun homeDao(): HomeDao
    abstract fun scriptDao(): ScriptDao
}
