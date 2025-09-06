package com.enxy.noolite.data.home.di

import com.enxy.noolite.data.home.HomeDbDataSourceImpl
import com.enxy.noolite.domain.common.HomeDbDataSource
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

object DataHomeModules {
    fun all(): List<Module> = listOf(
        dataSourceModule(),
    )

    private fun dataSourceModule(): Module = module {
        factoryOf(::HomeDbDataSourceImpl) bind HomeDbDataSource::class
    }
}
