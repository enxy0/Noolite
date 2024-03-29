package com.enxy.domain.features.script

import com.enxy.domain.features.common.Script
import com.enxy.domain.features.script.model.CreateScriptPayload
import kotlinx.coroutines.flow.Flow

interface ScriptDbDataSource {
    fun getScripts(): Flow<List<Script>>
    suspend fun createScript(payload: CreateScriptPayload)
    suspend fun removeScript(script: Script)
}
