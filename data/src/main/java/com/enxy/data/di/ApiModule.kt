package com.enxy.data.di

import com.enxy.data.net.NetworkFactory
import com.enxy.data.net.noolite.NooliteApi
import org.koin.dsl.module

internal val apiModule = module {
    single { NetworkFactory.createApi<NooliteApi>(get()) }
}
