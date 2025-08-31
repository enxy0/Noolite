package com.enxy.noolite.domain.script

import com.enxy.noolite.core.common.asResult
import com.enxy.noolite.core.model.UseCase
import com.enxy.noolite.domain.common.ScriptDbDataSource
import com.enxy.noolite.domain.common.model.CreateScriptPayload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface CreateScriptUseCase : UseCase<CreateScriptPayload, Unit>

internal class CreateScriptUseCaseImpl(
    private val scriptDbDataSource: ScriptDbDataSource
) : CreateScriptUseCase {
    override fun invoke(param: CreateScriptPayload): Flow<Result<Unit>> = flow {
        emit(scriptDbDataSource.createScript(param))
    }.asResult()
}
