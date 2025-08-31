package com.enxy.noolite.di

import com.enxy.noolite.core.ui.IntentActionsProvider
import com.enxy.noolite.utils.intent.IntentActionsProviderImpl
import com.enxy.noolite.utils.lifecycle.AppThemeObserver
import com.enxy.noolite.utils.lifecycle.AppThemeObserverImpl
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
        singleOf(::AppThemeObserverImpl) bind AppThemeObserver::class
    }
}
