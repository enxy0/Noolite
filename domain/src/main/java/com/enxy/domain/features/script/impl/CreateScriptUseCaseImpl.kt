package com.enxy.domain.features.script.impl

import com.enxy.domain.features.script.CreateScriptUseCase
import com.enxy.domain.features.script.ScriptDbDataSource
import com.enxy.domain.features.script.model.CreateScriptPayload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class CreateScriptUseCaseImpl(
    private val scriptDbDataSource: ScriptDbDataSource
) : CreateScriptUseCase {
    override fun execute(param: CreateScriptPayload): Flow<Result<Unit>> = flow {
        scriptDbDataSource.createScript(param)
        emit(Result.success(Unit))
    }
}
