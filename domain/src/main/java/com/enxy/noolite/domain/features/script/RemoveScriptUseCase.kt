package com.enxy.noolite.domain.features.script

import com.enxy.noolite.domain.UseCase
import com.enxy.noolite.domain.asResult
import com.enxy.noolite.domain.common.Script
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
