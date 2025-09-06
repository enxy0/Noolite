package com.enxy.noolite.core.database.di

import com.enxy.noolite.core.database.AppDatabase
import com.enxy.noolite.core.database.AppDatabaseFactory
import com.enxy.noolite.core.database.home.HomeDao
import com.enxy.noolite.core.database.script.ScriptDao
import com.enxy.noolite.core.database.settings.SettingsDao
import org.koin.core.module.Module
import org.koin.dsl.module

object CoreDatabaseModules {
    fun all(): List<Module> = listOf(
        databaseModule()
    )

    private fun databaseModule(): Module = module {
        single<AppDatabase> { AppDatabaseFactory.create(get()) }
        single<SettingsDao> { get<AppDatabase>().settingsDao() }
        single<HomeDao> { get<AppDatabase>().homeDao() }
        single<ScriptDao> { get<AppDatabase>().scriptDao() }
    }
}
