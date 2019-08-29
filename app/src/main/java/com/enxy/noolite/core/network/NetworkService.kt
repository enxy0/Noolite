package com.enxy.noolite.core.network

import com.enxy.noolite.core.platform.FileManager
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkService {
    private var retrofitInstance: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    companion object {
        var BASE_URL = "http://${FileManager.DEFAULT_IP_ADDRESS_VALUE}"
            set(value) {
                field = "http://$value"
            }
        val instance = NetworkService()
    }

    fun getNooliteApi(): NooliteApi = retrofitInstance.create(NooliteApi::class.java)
}