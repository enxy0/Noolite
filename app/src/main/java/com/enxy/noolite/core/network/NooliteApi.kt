package com.enxy.noolite.core.network

import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface NooliteApi {

    @POST
    @Streaming
    fun getGroupsAsync(@Url url: String): Deferred<Response<ResponseBody>>

    @GET
    fun changeLightsStateAsync(
        @Url url: String,
        @Query("ch") channelAddress: Int,
        @Query("cmd") command: Int
    ): Deferred<Response<ResponseBody>>

    @GET
    fun changeBacklightStateAsync(
        @Url url: String,
        @Query("ch") channelAddress: Int,
        @Query("cmd") command: Int,
        @Query("fm") fm: Int,
        @Query("br") brightness: Int
    ): Deferred<Response<ResponseBody>>
}