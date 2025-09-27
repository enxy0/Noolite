package com.enxy.noolite.di

import com.enxy.noolite.BuildConfig
import com.enxy.noolite.core.model.SharedBuildConfig
import com.enxy.noolite.core.ui.IntentActionsProvider
import com.enxy.noolite.utils.IntentActionsProviderImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

object PresentationModules {
    fun all(): List<Module> = listOf(
        commonModule(),
    )

    private fun commonModule() = module {
        singleOf(::IntentActionsProviderImpl) bind IntentActionsProvider::class
        single {
            SharedBuildConfig(
                versionCode = BuildConfig.VERSION_CODE,
                versionName = BuildConfig.VERSION_NAME,
            )
        }
    }
}
