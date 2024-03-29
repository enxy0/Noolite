package com.enxy.domain.features.settings

import com.enxy.domain.features.common.Group

interface NooliteDataSource {
    suspend fun getGroups(): List<Group>
    suspend fun toggleLight(channelId: Int)
    suspend fun turnOffLight(channelId: Int)
    suspend fun turnOnLight(channelId: Int)
    suspend fun changeBrightness(channelId: Int, brightness: Int)
    suspend fun startOverflow(channelId: Int)
    suspend fun stopOverflow(channelId: Int)
    suspend fun changeColor(channelId: Int)
}
