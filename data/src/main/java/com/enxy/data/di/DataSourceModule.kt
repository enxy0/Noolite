package com.enxy.data.di

import com.enxy.data.db.home.HomeDbDataSourceImpl
import com.enxy.data.db.script.ScriptDbDataSourceImpl
import com.enxy.data.db.settings.SettingsDbDataSourceImpl
import com.enxy.data.net.noolite.NooliteDataSourceImpl
import com.enxy.data.static.StaticInfoDataSourceImpl
import com.enxy.domain.features.demo.StaticInfoDataSource
import com.enxy.domain.features.home.HomeDbDataSource
import com.enxy.domain.features.script.ScriptDbDataSource
import com.enxy.domain.features.settings.NooliteDataSource
import com.enxy.domain.features.settings.SettingsDbDataSource
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val dataSourceModule = module {
    factoryOf(::SettingsDbDataSourceImpl) bind SettingsDbDataSource::class
    factoryOf(::HomeDbDataSourceImpl) bind HomeDbDataSource::class
    factoryOf(::ScriptDbDataSourceImpl) bind ScriptDbDataSource::class
    factoryOf(::NooliteDataSourceImpl) bind NooliteDataSource::class
    factoryOf(::StaticInfoDataSourceImpl) bind StaticInfoDataSource::class
}
