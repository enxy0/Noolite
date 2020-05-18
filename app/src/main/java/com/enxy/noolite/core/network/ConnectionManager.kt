package com.enxy.noolite.core.network

import android.annotation.SuppressLint
import android.content.Context
import android.net.wifi.WifiManager

/**
 * [ConnectionManager] is used to access WiFi state of the phone.
 */
class ConnectionManager(private val appContext: Context) {
    var isEnabled = true

    @SuppressLint("WifiManagerPotentialLeak")
    fun isWifiConnected(): Boolean {
        return if (this.isEnabled) {
            val wifiMgr =
                appContext.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
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