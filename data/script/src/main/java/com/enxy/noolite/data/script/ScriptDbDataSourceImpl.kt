package com.enxy.noolite.data.script

import com.enxy.noolite.core.database.script.ScriptDao
import com.enxy.noolite.core.database.script.model.ScriptEntity
import com.enxy.noolite.core.database.script.toDomain
import com.enxy.noolite.core.database.script.toEntity
import com.enxy.noolite.core.model.Script
import com.enxy.noolite.domain.common.ScriptDbDataSource
import com.enxy.noolite.domain.common.model.CreateScriptPayload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class ScriptDbDataSourceImpl(
    private val scriptDao: ScriptDao
) : ScriptDbDataSource {
    override fun getScripts(): Flow<List<Script>> {
        return scriptDao.getScripts().map { list -> list.map { entity -> entity.toDomain() } }
    }

    override suspend fun createScript(payload: CreateScriptPayload) {
        val script = ScriptEntity(
            name = payload.name,
            actions = payload.actions.map { action -> action.toEntity() }
        )
        scriptDao.insertScript(script)
    }

    override suspend fun removeScript(script: Script) {
        scriptDao.deleteScript(script.toEntity())
    }
}
