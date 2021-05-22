package com.plandora.activity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi

class NetworkCheck(private val context: Context) {

    companion object {
        var isNetworkConnected = false
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun registerNetworkCallback() {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        NetworkRequest.Builder()
        connectivityManager.registerDefaultNetworkCallback( object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                isNetworkConnected = true
            }
            override fun onLost(network: Network) {
                isNetworkConnected = false
            }
        })
    }

}