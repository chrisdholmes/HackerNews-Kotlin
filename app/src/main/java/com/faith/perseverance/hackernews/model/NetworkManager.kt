package com.faith.perseverance.hackernews.model

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class NetworkManager(val context: Context) {

    val URL = "http://hn.algolia.com/api/v1/search?tags=front_page" ///

    private val TAG = "NetworkManager: "

    var connected: Boolean = isNetworkConnected()
    var data: List<Article>? = mutableListOf()


    private fun isNetworkConnected(): Boolean {
        //1
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        //2

        val activeNetwork = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            connectivityManager.activeNetwork
        } else {
            TODO("VERSION.SDK_INT < M")
        }
        //3
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        //4

        return networkCapabilities != null &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }





}