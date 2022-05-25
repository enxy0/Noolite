package com.enxy.data.net

import com.enxy.domain.features.settings.SettingsDbDataSource
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalSerializationApi::class)
internal object NetworkFactory {

    inline fun <reified T> createApi(retrofit: Retrofit): T = retrofit.create(T::class.java)

    fun createRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .addConverterFactory(Json.asConverterFactory(contentType))
            .client(okHttpClient)
            .baseUrl("http://localhost/")
            .build()
    }

    fun createOkHttpClient(vararg interceptors: Interceptor): OkHttpClient {
        return interceptors
            .fold(OkHttpClient.Builder()) { builder, interceptor ->
                builder.addInterceptor(interceptor)
            }
            .connectTimeout(5L, TimeUnit.SECONDS)
            .readTimeout(60L, TimeUnit.SECONDS)
            .writeTimeout(60L, TimeUnit.SECONDS)
            .build()
    }

    fun createLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    fun createDynamicHostInterceptor(settingsDbDataSource: SettingsDbDataSource): Interceptor {
        return DynamicHostInterceptor(settingsDbDataSource.getAppSettingsFlow())
    }
}
