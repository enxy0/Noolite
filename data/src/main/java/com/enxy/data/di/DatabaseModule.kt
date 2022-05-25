package com.enxy.data.di

import com.enxy.data.db.AppDatabase
import com.enxy.data.db.AppDatabaseFactory
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

internal val databaseModule = module {
    single { AppDatabaseFactory.create(androidApplication()) }
    single { get<AppDatabase>().settingsDao() }
    single { get<AppDatabase>().homeDao() }
    single { get<AppDatabase>().scriptDao() }
}
