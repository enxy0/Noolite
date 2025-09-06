package com.enxy.noolite.core.database.script

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.enxy.noolite.core.database.script.model.ScriptEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScriptDao {
    @Query("SELECT * FROM Script")
    fun getScripts(): Flow<List<ScriptEntity>>

    @Insert
    suspend fun insertScript(script: ScriptEntity)

    @Delete
    suspend fun deleteScript(script: ScriptEntity)
}
