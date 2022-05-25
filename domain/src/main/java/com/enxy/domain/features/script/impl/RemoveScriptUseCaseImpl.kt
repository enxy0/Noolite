package com.enxy.domain.features.script.impl

import com.enxy.domain.features.common.Script
import com.enxy.domain.features.script.RemoveScriptUseCase
import com.enxy.domain.features.script.ScriptDbDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class RemoveScriptUseCaseImpl(
    private val scriptDbDataSource: ScriptDbDataSource
) : RemoveScriptUseCase {
    override fun execute(param: Script): Flow<Result<Unit>> = flow {
        scriptDbDataSource.removeScript(param)
        emit(Result.success(Unit))
    }
}
