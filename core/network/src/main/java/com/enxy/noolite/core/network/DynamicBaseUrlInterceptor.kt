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

internal class DynamicBaseUrlInterceptor(
    settingsFlow: () -> Flow<AppSettings>
) : Interceptor {

    private val settingsFlow by lazy(settingsFlow)

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val apiUrl = runBlocking { settingsFlow.first() }.apiUrl
        val newUrl = try {
            mergeBaseIntoOriginalUrl(apiUrl, originalRequest.url)
        } catch (e: IOException) {
            throw e
        }
        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()
        return chain.proceed(newRequest)
    }

    private fun mergeBaseIntoOriginalUrl(apiUrl: String, original: HttpUrl): HttpUrl {
        val hasScheme = apiUrl.contains("://")
        val withScheme = if (!hasScheme) "http://$apiUrl" else apiUrl
        val base = withScheme.toHttpUrlOrNull()
            ?: throw IOException("Invalid api url configuration: $apiUrl")
        val schemeToUse = if (hasScheme) base.scheme else original.scheme
        val portProvidedRegex = """:\d+(/|$)""".toRegex()
        val portProvided = portProvidedRegex.containsMatchIn(apiUrl)
        val portToUse = if (portProvided) base.port else original.port
        val basePathRaw = base.encodedPath
        val originalPathRaw = original.encodedPath
        val basePathTrimmed = basePathRaw.trimEnd('/')
        val originalPathTrimmed = originalPathRaw.trimStart('/')
        val combinedPath = when {
            basePathTrimmed.isEmpty() && originalPathTrimmed.isEmpty() -> {
                "/"
            }
            basePathTrimmed.isEmpty() -> {
                "/$originalPathTrimmed"
            }
            originalPathTrimmed.isEmpty() -> {
                if (basePathTrimmed.startsWith("/")) basePathTrimmed else "/$basePathTrimmed"
            }
            else -> {
                val prefix =
                    if (basePathTrimmed.startsWith("/")) basePathTrimmed else "/$basePathTrimmed"
                "$prefix/$originalPathTrimmed"
            }
        }
        return original.newBuilder()
            .scheme(schemeToUse)
            .host(base.host)
            .port(portToUse)
            .encodedPath(combinedPath)
            .build()
    }
}
