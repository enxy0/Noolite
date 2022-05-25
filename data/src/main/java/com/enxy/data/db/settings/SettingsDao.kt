package com.enxy.data.db.settings

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.enxy.data.db.settings.model.SettingsEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface SettingsDao {
    @Query("SELECT * FROM Settings")
    fun getSettingsFlow(): Flow<SettingsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSettings(settings: SettingsEntity)
}
