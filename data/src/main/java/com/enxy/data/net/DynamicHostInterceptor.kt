package com.enxy.data.net

import com.enxy.domain.features.settings.model.AppSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class DynamicHostInterceptor(
    private val settingsFlow: Flow<AppSettings>
) : Interceptor {
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
