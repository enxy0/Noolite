package com.enxy.noolite.core.network

import com.enxy.noolite.core.utils.Constants.Companion.DEFAULT_IP_ADDRESS_VALUE
import retrofit2.Retrofit

class NetworkService(private val retrofit: Retrofit) {

    companion object {
        var BASE_URL = "http://${DEFAULT_IP_ADDRESS_VALUE}"
            set(value) {
                field = "http://$value"
            }
    }

    fun getNooliteApi(): NooliteApi = retrofit.create(NooliteApi::class.java)
}