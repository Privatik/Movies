package com.io.movies.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


object Config {
    const val photo = "https://image.tmdb.org/t/p/w500"
    const val apiKey = "f0c8cfc8c338e88d2157cc91dc4b893c"
    const val imdb = "https://www.imdb.com/title/"


    var isConnect: Boolean? = null

    private var connectivityManager: ConnectivityManager? = null

    fun isOnline(context: Context? = null): LiveData<Boolean>?{
        if (connectivityManager == null && context == null) return null
        if (connectivityManager == null) connectivityManager = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return isOnline
    }

    private val isOnline: LiveData<Boolean> by lazy {
        MutableLiveData<Boolean>().apply {
            val networkRequest = NetworkRequest.Builder().run {
                addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                build()
            }

            connectivityManager!!.registerNetworkCallback(networkRequest,
                object : ConnectivityManager.NetworkCallback (){
                    override fun onAvailable(network: Network) {
                        super.onAvailable(network)
                        isConnect = true
                        postValue(true)
                    }

                    override fun onLost(network: Network) {
                        super.onLost(network)
                        isConnect = false
                        postValue(false)
                    }
                })
            if (value == null) {
                isConnect = false
                postValue(false)
            }
        }
    }
}