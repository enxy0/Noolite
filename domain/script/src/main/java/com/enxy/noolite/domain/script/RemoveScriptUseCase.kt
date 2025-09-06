package com.enxy.noolite.domain.script

import com.enxy.noolite.core.common.asResult
import com.enxy.noolite.core.model.Script
import com.enxy.noolite.core.model.UseCase
import com.enxy.noolite.domain.common.ScriptDbDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface RemoveScriptUseCase : UseCase<Script, Unit>

internal class RemoveScriptUseCaseImpl(
    private val scriptDbDataSource: ScriptDbDataSource
) : RemoveScriptUseCase {
    override fun invoke(param: Script): Flow<Result<Unit>> = flow {
        emit(scriptDbDataSource.removeScript(param))
    }.asResult()
}
