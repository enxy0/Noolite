package com.enxy.noolite.di

import com.enxy.noolite.di.modules.commonModule
import org.koin.core.module.Module

object PresentationModules {
    fun all(): List<Module> = listOf(
        commonModule,
    )
}
