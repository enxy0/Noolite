package com.enxy.noolite.core.network.api

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Streaming

interface NooliteApi {
    @POST("noolite_settings.bin")
    @Streaming
    suspend fun getSettings(): ResponseBody

    @GET("api.htm")
    suspend fun changeLightState(
        @Query("ch") channelId: Int,
        @Query("cmd") command: Int
    ): ResponseBody

    @GET("api.htm")
    suspend fun changeBacklightState(
        @Query("ch") channelId: Int,
        @Query("cmd") command: Int,
        @Query("fm") fm: Int,
        @Query("br") brightness: Int
    ): ResponseBody
}
