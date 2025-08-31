package com.enxy.noolite.data.settings.di

import com.enxy.noolite.data.settings.SettingsDataSourceImpl
import com.enxy.noolite.data.settings.static.StaticInfoDataSourceImpl
import com.enxy.noolite.domain.settings.SettingsDataSource
import com.enxy.noolite.domain.settings.StaticInfoDataSource
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

object DataSettingsModules {
    fun all(): List<Module> = listOf(
        dataSourceModule(),
    )

    fun dataSourceModule(): Module = module {
        factoryOf(::StaticInfoDataSourceImpl) bind StaticInfoDataSource::class
        factoryOf(::SettingsDataSourceImpl) bind SettingsDataSource::class
    }
}
