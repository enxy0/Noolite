package com.enxy.noolite.domain.common

interface NooliteDataSource {
    suspend fun toggleLight(channelId: Int)
    suspend fun turnOffLight(channelId: Int)
    suspend fun turnOnLight(channelId: Int)
    suspend fun changeBrightness(channelId: Int, brightness: Int)
    suspend fun startOverflow(channelId: Int)
    suspend fun stopOverflow(channelId: Int)
    suspend fun changeColor(channelId: Int)
}
