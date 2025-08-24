package com.enxy.noolite.domain.features.script.impl

import com.enxy.noolite.domain.features.actions.ChannelActionUseCase
import com.enxy.noolite.domain.features.common.Script
import com.enxy.noolite.domain.features.script.ExecuteScriptUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class ExecuteScriptUseCaseImpl(
    private val channelActionUseCase: ChannelActionUseCase
) : ExecuteScriptUseCase {
    override fun execute(param: Script): Flow<Result<Unit>> = flow {
        for (action in param.actions) {
            val result = channelActionUseCase(action).first()
            if (result.isFailure) {
                emit(result)
            }
        }
    }
}
