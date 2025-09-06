package com.enxy.noolite.domain.settings.di

import com.enxy.noolite.domain.common.GetAppSettingsUseCase
import com.enxy.noolite.domain.settings.GetAppSettingsUseCaseImpl
import com.enxy.noolite.domain.settings.GetNooliteGroupsUseCase
import com.enxy.noolite.domain.settings.GetNooliteGroupsUseCaseImpl
import com.enxy.noolite.domain.settings.SetDemoInfoUseCase
import com.enxy.noolite.domain.settings.SetDemoInfoUseCaseImpl
import com.enxy.noolite.domain.settings.UpdateAppSettingsUseCase
import com.enxy.noolite.domain.settings.UpdateAppSettingsUseCaseImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

object DomainSettingsModules {
    fun all(): List<Module> = listOf(
        useCaseModule(),
    )

    private fun useCaseModule(): Module = module {
        factoryOf(::GetAppSettingsUseCaseImpl) bind GetAppSettingsUseCase::class
        factoryOf(::GetNooliteGroupsUseCaseImpl) bind GetNooliteGroupsUseCase::class
        factoryOf(::UpdateAppSettingsUseCaseImpl) bind UpdateAppSettingsUseCase::class
        factoryOf(::SetDemoInfoUseCaseImpl) bind SetDemoInfoUseCase::class
    }
}
