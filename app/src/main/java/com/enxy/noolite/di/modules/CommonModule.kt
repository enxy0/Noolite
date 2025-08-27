package com.enxy.noolite.di.modules

import com.enxy.noolite.utils.intent.IntentActionsProvider
import com.enxy.noolite.utils.intent.IntentActionsProviderImpl
import com.enxy.noolite.utils.lifecycle.AppThemeObserver
import com.enxy.noolite.utils.lifecycle.AppThemeObserverImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val commonModule = module {
    singleOf(::IntentActionsProviderImpl) bind IntentActionsProvider::class
    singleOf(::AppThemeObserverImpl) bind AppThemeObserver::class
}
