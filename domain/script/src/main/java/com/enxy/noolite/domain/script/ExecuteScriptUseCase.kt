package com.enxy.noolite.domain.script

import com.enxy.noolite.core.common.asResult
import com.enxy.noolite.core.model.Script
import com.enxy.noolite.core.model.UseCase
import com.enxy.noolite.domain.common.ChannelActionUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

interface ExecuteScriptUseCase : UseCase<Script, Unit>

internal class ExecuteScriptUseCaseImpl(
    private val channelActionUseCase: ChannelActionUseCase
) : ExecuteScriptUseCase {
    override fun invoke(param: Script): Flow<Result<Unit>> = flow {
        for (action in param.actions) {
            channelActionUseCase(action).first().getOrThrow()
        }
        emit(Unit)
    }.asResult()
}
