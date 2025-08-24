package com.enxy.noolite.data.di

import com.enxy.noolite.data.net.NetworkFactory
import com.enxy.noolite.data.net.noolite.NooliteApi
import org.koin.dsl.module

internal val apiModule = module {
    single { NetworkFactory.createApi<NooliteApi>(get()) }
}
