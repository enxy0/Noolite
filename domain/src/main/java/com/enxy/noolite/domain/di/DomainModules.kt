package com.enxy.noolite.domain.di

import org.koin.core.module.Module

object DomainModules {
    fun all(): List<Module> = listOf(useCaseModule)
}
