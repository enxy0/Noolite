package com.enxy.data.db.script

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.enxy.data.db.script.model.ScriptEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScriptDao {
    @Query("SELECT * FROM Script")
    fun getScripts(): Flow<List<ScriptEntity>>

    @Insert
    fun insertScript(script: ScriptEntity)

    @Delete
    fun deleteScript(script: ScriptEntity)
}
