package com.enxy.noolite.core.network

import com.enxy.noolite.core.model.AppSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Response
import okio.IOException

internal class DynamicHostInterceptor(
    settingsFlow: () -> Flow<AppSettings>
) : Interceptor {

    private val settingsFlow by lazy(settingsFlow)

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val apiUrl = runBlocking { settingsFlow.first() }.apiUrl
        request = request
            .newBuilder()
            .url(normalizeUrl(apiUrl))
            .build()
        return chain.proceed(request)
    }

    private fun normalizeUrl(apiUrl: String): HttpUrl {
        val withScheme = if (!apiUrl.contains("://")) "http://$apiUrl" else apiUrl
        return withScheme.toHttpUrlOrNull() ?: throw IOException("Invalid api url configuration!")
    }
}
