package com.enxy.data.di

import org.koin.core.module.Module

object DataModules {
    fun all(): List<Module> = listOf(
        networkModule,
        apiModule,
        databaseModule,
        dataSourceModule
    )
}
