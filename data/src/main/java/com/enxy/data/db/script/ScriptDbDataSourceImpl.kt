package com.enxy.data.db.script

import com.enxy.data.db.script.model.ScriptEntity
import com.enxy.domain.features.common.Script
import com.enxy.domain.features.script.ScriptDbDataSource
import com.enxy.domain.features.script.model.CreateScriptPayload
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
