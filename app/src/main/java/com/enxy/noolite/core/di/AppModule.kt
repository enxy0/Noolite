package com.enxy.noolite.core.di

import com.enxy.noolite.core.network.ConnectionManager
import com.enxy.noolite.core.network.NetworkService
import com.enxy.noolite.core.network.Repository
import com.enxy.noolite.core.utils.FileManager
import com.enxy.noolite.features.MainViewModel
import com.enxy.noolite.features.settings.SettingsManager
import com.enxy.noolite.features.settings.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    factory { FileManager(androidContext()) }
    factory { NetworkService(get()) }
    single { SettingsManager(get(), get()) }
    single { ConnectionManager(androidContext()) }
    single { Repository(get(), get(), get(), get()) }
    viewModel { MainViewModel(get(), get(), get()) }
    viewModel { SettingsViewModel(get(), get()) }
}