package com.enxy.noolite.data.di

import com.enxy.noolite.data.db.home.HomeDbDataSourceImpl
import com.enxy.noolite.data.db.script.ScriptDbDataSourceImpl
import com.enxy.noolite.data.db.settings.SettingsDbDataSourceImpl
import com.enxy.noolite.data.net.noolite.NooliteDataSourceImpl
import com.enxy.noolite.data.static.StaticInfoDataSourceImpl
import com.enxy.noolite.domain.features.demo.StaticInfoDataSource
import com.enxy.noolite.domain.features.home.HomeDbDataSource
import com.enxy.noolite.domain.features.script.ScriptDbDataSource
import com.enxy.noolite.domain.features.settings.NooliteDataSource
import com.enxy.noolite.domain.features.settings.SettingsDbDataSource
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
