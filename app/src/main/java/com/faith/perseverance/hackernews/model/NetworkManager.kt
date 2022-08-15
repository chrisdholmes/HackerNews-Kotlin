package com.faith.perseverance.hackernews.model

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

/**
 * NetworkManager verifies the device is connected to the Internet.
 * Device must be connected to the internet before network calls are made.
 *
 * @param context
 * @property connected
 */
class NetworkManager(val context: Context) {

    private val TAG = "NetworkManager: "

    var connected: Boolean = isNetworkConnected()

    private fun isNetworkConnected(): Boolean {

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork

        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)

        return networkCapabilities != null &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }


}