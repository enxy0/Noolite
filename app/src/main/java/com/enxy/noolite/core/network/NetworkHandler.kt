package com.enxy.noolite.core.network

import android.annotation.SuppressLint
import android.content.Context
import android.net.wifi.WifiManager

class NetworkHandler(private val context: Context) {
    @SuppressLint("WifiManagerPotentialLeak")
    fun isWifiConnected(): Boolean {
        val wifiMgr = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return if (wifiMgr.isWifiEnabled) {
            wifiMgr.connectionInfo.ipAddress != 0
        } else {
            false
        }
    }
}