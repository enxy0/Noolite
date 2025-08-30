package com.enxy.noolite.domain.features.script

import com.enxy.noolite.domain.UseCase
import com.enxy.noolite.domain.asResult
import com.enxy.noolite.domain.features.script.model.CreateScriptPayload
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
