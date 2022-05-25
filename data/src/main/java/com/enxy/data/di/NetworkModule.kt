package com.enxy.data.di

import com.enxy.data.net.NetworkFactory
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val LOGGING_INTERCEPTOR = "logging_interceptor"
private const val DYNAMIC_HOST_INTERCEPTOR = "dynamic_host_interceptor"

internal val networkModule = module {
    singleOf(NetworkFactory::createRetrofit)
    single {
        NetworkFactory.createOkHttpClient(
            get(named(DYNAMIC_HOST_INTERCEPTOR)),
            get(named(LOGGING_INTERCEPTOR))
        )
    }
    single(named(LOGGING_INTERCEPTOR)) { NetworkFactory.createLoggingInterceptor() }
    single(named(DYNAMIC_HOST_INTERCEPTOR)) { NetworkFactory.createDynamicHostInterceptor(get()) }
}
