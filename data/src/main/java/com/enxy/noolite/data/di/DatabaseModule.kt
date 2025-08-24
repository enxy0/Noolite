package com.enxy.noolite.data.di

import com.enxy.noolite.data.db.AppDatabase
import com.enxy.noolite.data.db.AppDatabaseFactory
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

internal val databaseModule = module {
    single { AppDatabaseFactory.create(androidApplication()) }
    single { get<AppDatabase>().settingsDao() }
    single { get<AppDatabase>().homeDao() }
    single { get<AppDatabase>().scriptDao() }
}
