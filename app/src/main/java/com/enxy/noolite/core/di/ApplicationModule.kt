package com.enxy.noolite.core.di

import android.content.Context
import com.enxy.noolite.AndroidApplication
import com.enxy.noolite.core.network.NetworkService
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: AndroidApplication) {

    @Provides
    @Singleton
    fun provideApplicationContext(): Context = application

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(NetworkService.BASE_URL)
        .client(
            OkHttpClient().newBuilder()
                .connectTimeout(1, TimeUnit.SECONDS)
                .build()
        )
        .build()

    @Provides
    fun provideGson(): Gson = Gson()
}