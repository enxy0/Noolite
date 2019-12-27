package com.enxy.noolite.core.network

import com.enxy.noolite.core.platform.FileManager
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkService @Inject constructor(private val retrofit: Retrofit) {

    companion object {
        var BASE_URL = "http://${FileManager.DEFAULT_IP_ADDRESS_VALUE}"
            set(value) {
                field = "http://$value"
            }
    }

    fun getNooliteApi(): NooliteApi = retrofit.create(NooliteApi::class.java)
}