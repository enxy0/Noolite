package com.enxy.noolite.core.network

import com.enxy.noolite.core.model.AppSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

interface NetworkFactory {
    fun <T> createApi(retrofit: Retrofit, api: Class<T>): T
    fun createRetrofit(okHttpClient: OkHttpClient): Retrofit
    fun createOkHttpClient(vararg interceptors: Interceptor): OkHttpClient
    fun createLoggingInterceptor(): Interceptor
    fun createDynamicHostInterceptor(settingsFlow: () -> Flow<AppSettings>): Interceptor
}

@OptIn(ExperimentalSerializationApi::class)
internal class NetworkFactoryImpl : NetworkFactory {

    override fun <T> createApi(retrofit: Retrofit, api: Class<T>): T = retrofit.create(api)

    override fun createRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .addConverterFactory(Json.asConverterFactory(contentType))
            .client(okHttpClient)
            .baseUrl("http://localhost/")
            .build()
    }

    override fun createOkHttpClient(vararg interceptors: Interceptor): OkHttpClient {
        return interceptors
            .fold(OkHttpClient.Builder()) { builder, interceptor ->
                builder.addInterceptor(interceptor)
            }
            .connectTimeout(5L, TimeUnit.SECONDS)
            .readTimeout(60L, TimeUnit.SECONDS)
            .writeTimeout(60L, TimeUnit.SECONDS)
            .build()
    }

    override fun createLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    override fun createDynamicHostInterceptor(settingsFlow: () -> Flow<AppSettings>): Interceptor {
        return DynamicHostInterceptor(settingsFlow)
    }
}
