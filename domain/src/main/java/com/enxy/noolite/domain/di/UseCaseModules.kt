package com.enxy.noolite.domain.di

import com.enxy.noolite.domain.features.actions.ChannelActionUseCase
import com.enxy.noolite.domain.features.actions.GroupActionUseCase
import com.enxy.noolite.domain.features.actions.impl.ChannelActionUseCaseImpl
import com.enxy.noolite.domain.features.actions.impl.GroupActionUseCaseImpl
import com.enxy.noolite.domain.features.demo.SetDemoInfoUseCase
import com.enxy.noolite.domain.features.demo.SetDemoInfoUseCaseImpl
import com.enxy.noolite.domain.features.home.GetFavoriteGroupUseCase
import com.enxy.noolite.domain.features.home.GetHomeDataUseCase
import com.enxy.noolite.domain.features.home.SetFavoriteGroupUseCase
import com.enxy.noolite.domain.features.home.impl.GetFavoriteGroupUseCaseImpl
import com.enxy.noolite.domain.features.home.impl.GetHomeDataUseCaseImpl
import com.enxy.noolite.domain.features.home.impl.SetFavoriteGroupUseCaseImpl
import com.enxy.noolite.domain.features.script.CreateScriptUseCase
import com.enxy.noolite.domain.features.script.ExecuteScriptUseCase
import com.enxy.noolite.domain.features.script.GetGroupsUseCase
import com.enxy.noolite.domain.features.script.RemoveScriptUseCase
import com.enxy.noolite.domain.features.script.impl.CreateScriptUseCaseImpl
import com.enxy.noolite.domain.features.script.impl.ExecuteScriptUseCaseImpl
import com.enxy.noolite.domain.features.script.impl.GetGroupsUseCaseImpl
import com.enxy.noolite.domain.features.script.impl.RemoveScriptUseCaseImpl
import com.enxy.noolite.domain.features.settings.GetAppSettingsUseCase
import com.enxy.noolite.domain.features.settings.GetNooliteGroupsUseCase
import com.enxy.noolite.domain.features.settings.UpdateAppSettingsUseCase
import com.enxy.noolite.domain.features.settings.impl.GetAppSettingsUseCaseImpl
import com.enxy.noolite.domain.features.settings.impl.GetNooliteGroupsUseCaseImpl
import com.enxy.noolite.domain.features.settings.impl.UpdateAppSettingsUseCaseImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val useCaseModule = module {
    factoryOf(::GetHomeDataUseCaseImpl) bind GetHomeDataUseCase::class
    factoryOf(::GetAppSettingsUseCaseImpl) bind GetAppSettingsUseCase::class
    factoryOf(::UpdateAppSettingsUseCaseImpl) bind UpdateAppSettingsUseCase::class
    factoryOf(::GetFavoriteGroupUseCaseImpl) bind GetFavoriteGroupUseCase::class
    factoryOf(::SetFavoriteGroupUseCaseImpl) bind SetFavoriteGroupUseCase::class
    factoryOf(::ChannelActionUseCaseImpl) bind ChannelActionUseCase::class
    factoryOf(::GroupActionUseCaseImpl) bind GroupActionUseCase::class
    factoryOf(::GetNooliteGroupsUseCaseImpl) bind GetNooliteGroupsUseCase::class
    factoryOf(::GetGroupsUseCaseImpl) bind GetGroupsUseCase::class
    factoryOf(::CreateScriptUseCaseImpl) bind CreateScriptUseCase::class
    factoryOf(::RemoveScriptUseCaseImpl) bind RemoveScriptUseCase::class
    factoryOf(::ExecuteScriptUseCaseImpl) bind ExecuteScriptUseCase::class
    factoryOf(::SetDemoInfoUseCaseImpl) bind SetDemoInfoUseCase::class
}
