package com.enxy.noolite.core.network

import com.enxy.noolite.core.model.AppSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

internal class DynamicHostInterceptor(
    settingsFlow: () -> Flow<AppSettings>
) : Interceptor {

    private val settingsFlow by lazy(settingsFlow)

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val host = runBlocking { settingsFlow.first() }.apiUrl
        val url = request.url
            .newBuilder()
            .host(host)
            .build()
        request = request
            .newBuilder()
            .url(url)
            .build()
        return chain.proceed(request)
    }
}
