package com.enxy.noolite.data.net

import retrofit2.Response

internal interface NetworkDataSource {
    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun <T> request(call: suspend () -> Response<T>): T {
        val response = call()
        return if (response.isSuccessful) {
            response.body() ?: throw ApiException(code = response.code())
        } else {
            throw ApiException(code = response.code(), message = response.errorBody()?.string())
        }
    }
}
