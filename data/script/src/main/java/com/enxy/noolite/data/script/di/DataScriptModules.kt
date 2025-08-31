package com.enxy.noolite.data.script.di

import com.enxy.noolite.data.script.NooliteDataSourceImpl
import com.enxy.noolite.data.script.ScriptDbDataSourceImpl
import com.enxy.noolite.domain.common.NooliteDataSource
import com.enxy.noolite.domain.common.ScriptDbDataSource
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

object DataScriptModules {
    fun all(): List<Module> = listOf(
        dataSourceModule(),
    )

    private fun dataSourceModule(): Module = module {
        factoryOf(::ScriptDbDataSourceImpl) bind ScriptDbDataSource::class
        factoryOf(::NooliteDataSourceImpl) bind NooliteDataSource::class
    }
}
