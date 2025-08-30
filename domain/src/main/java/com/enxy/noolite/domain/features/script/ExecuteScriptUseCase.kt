package com.enxy.noolite.domain.features.script

import com.enxy.noolite.domain.UseCase
import com.enxy.noolite.domain.asResult
import com.enxy.noolite.domain.common.Script
import com.enxy.noolite.domain.features.actions.ChannelActionUseCase
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
