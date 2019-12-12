package com.enxy.noolite.core.network

import android.annotation.SuppressLint
import android.content.Context
import android.net.wifi.WifiManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConnectionManager @Inject constructor(private val appContext: Context) {
    var isEnabled = true

    /* There is no leak, as appContext (provided by dagger) is the Application Context instance */
    @SuppressLint("WifiManagerPotentialLeak")
    fun isWifiConnected(): Boolean {
        return if (this.isEnabled) {
            val wifiMgr = appContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            if (wifiMgr.isWifiEnabled) {
                val isConnected = wifiMgr.connectionInfo.ipAddress != 0
                isConnected
            } else {
                false
            }
        } else
            true
    }
}