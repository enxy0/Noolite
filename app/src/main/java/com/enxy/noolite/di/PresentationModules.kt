package com.enxy.noolite.di

import com.enxy.noolite.di.modules.commonModule
import com.enxy.noolite.di.modules.detailsModule
import com.enxy.noolite.di.modules.homeModule
import com.enxy.noolite.di.modules.scriptModule
import com.enxy.noolite.di.modules.settingsModule
import org.koin.core.module.Module

object PresentationModules {
    fun all(): List<Module> = listOf(
        commonModule,
        homeModule,
        settingsModule,
        detailsModule,
        scriptModule
    )
}
