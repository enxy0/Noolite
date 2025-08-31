package com.enxy.noolite.core.network.di

import com.enxy.noolite.core.network.NetworkFactory
import com.enxy.noolite.core.network.NetworkFactoryImpl
import com.enxy.noolite.core.network.api.NooliteApi
import com.enxy.noolite.core.network.api.NooliteBinParser
import com.enxy.noolite.domain.common.GetAppSettingsUseCase
import kotlinx.coroutines.flow.mapNotNull
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

object CoreNetworkModules {
    private const val LOGGING_INTERCEPTOR = "logging_interceptor"
    private const val DYNAMIC_HOST_INTERCEPTOR = "dynamic_host_interceptor"

    fun all(): List<Module> = listOf(
        network(),
        nooliteApi(),
    )

    private fun nooliteApi() = module {
        singleOf<NooliteBinParser>(::NooliteBinParser)
        single<NooliteApi> { get<NetworkFactory>().createApi(get(), NooliteApi::class.java) }
    }

    private fun network() = module {
        singleOf<NetworkFactory>(::NetworkFactoryImpl)
        singleOf<Retrofit, _, _>(NetworkFactory::createRetrofit)
        single<OkHttpClient> {
            get<NetworkFactory>().createOkHttpClient(
                get(named(DYNAMIC_HOST_INTERCEPTOR)),
                get(named(LOGGING_INTERCEPTOR))
            )
        }
        single<Interceptor>(named(LOGGING_INTERCEPTOR)) {
            get<NetworkFactory>().createLoggingInterceptor()
        }
        single<Interceptor>(named(DYNAMIC_HOST_INTERCEPTOR)) {
            get<NetworkFactory>().createDynamicHostInterceptor(
                settingsFlow = {
                    get<GetAppSettingsUseCase>()
                        .invoke(Unit)
                        .mapNotNull { it.getOrNull() }
                },
            )
        }
    }
}
