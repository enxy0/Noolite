package com.enxy.noolite.core.di

import com.enxy.noolite.core.network.NetworkService
import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

val networkModule = module {
    single {
        Retrofit
            .Builder()
            .baseUrl(NetworkService.BASE_URL)
            .client(get())
            .build()
    }

    factory {
        OkHttpClient()
            .newBuilder()
            .connectTimeout(1, TimeUnit.SECONDS)
            .build()
    }

    factory { Gson() }
}