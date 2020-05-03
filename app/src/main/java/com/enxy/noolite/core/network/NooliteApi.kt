package com.enxy.noolite.core.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface NooliteApi {
    @POST
    @Streaming
    suspend fun getGroupsAsync(@Url url: String): Response<ResponseBody>

    @GET
    suspend fun changeLightsStateAsync(
        @Url url: String,
        @Query("ch") channelAddress: Int,
        @Query("cmd") command: Int
    ): Response<ResponseBody>

    @GET
    suspend fun changeBacklightStateAsync(
        @Url url: String,
        @Query("ch") channelAddress: Int,
        @Query("cmd") command: Int,
        @Query("fm") fm: Int,
        @Query("br") brightness: Int
    ): Response<ResponseBody>
}