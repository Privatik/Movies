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


    fun isOnline(context: Context): LiveData<Boolean> {
        val liveData: MutableLiveData<Boolean> = MutableLiveData(true)

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val builder = NetworkRequest.Builder()
        builder.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        builder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI)

        val networkRequest = builder.build()
        connectivityManager.registerNetworkCallback(networkRequest,
            object : ConnectivityManager.NetworkCallback (){
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    liveData.postValue(true)
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    liveData.postValue(false)
                }
            })

        return liveData
    }
}