package com.enxy.noolite.domain.common

import com.enxy.noolite.core.model.Script
import com.enxy.noolite.domain.common.model.CreateScriptPayload
import kotlinx.coroutines.flow.Flow

interface ScriptDbDataSource {
    fun getScripts(): Flow<List<Script>>
    suspend fun createScript(payload: CreateScriptPayload)
    suspend fun removeScript(script: Script)
}
