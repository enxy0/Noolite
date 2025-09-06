package com.enxy.noolite.domain.script.di

import com.enxy.noolite.domain.common.ChannelActionUseCase
import com.enxy.noolite.domain.script.ChannelActionUseCaseImpl
import com.enxy.noolite.domain.script.CreateScriptUseCase
import com.enxy.noolite.domain.script.CreateScriptUseCaseImpl
import com.enxy.noolite.domain.script.ExecuteScriptUseCase
import com.enxy.noolite.domain.script.ExecuteScriptUseCaseImpl
import com.enxy.noolite.domain.script.GetGroupsUseCase
import com.enxy.noolite.domain.script.GetGroupsUseCaseImpl
import com.enxy.noolite.domain.script.RemoveScriptUseCase
import com.enxy.noolite.domain.script.RemoveScriptUseCaseImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

object DomainScriptModules {

    fun all(): List<Module> = listOf(
        useCaseModule(),
    )

    private fun useCaseModule(): Module = module {
        factoryOf(::ChannelActionUseCaseImpl) bind ChannelActionUseCase::class
        factoryOf(::CreateScriptUseCaseImpl) bind CreateScriptUseCase::class
        factoryOf(::ExecuteScriptUseCaseImpl) bind ExecuteScriptUseCase::class
        factoryOf(::GetGroupsUseCaseImpl) bind GetGroupsUseCase::class
        factoryOf(::RemoveScriptUseCaseImpl) bind RemoveScriptUseCase::class
    }
}
