package com.io.movies.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.core.content.ContextCompat.getSystemService


object Config {
    const val photo = "https://image.tmdb.org/t/p/w500"
    const val apiKey = "f0c8cfc8c338e88d2157cc91dc4b893c"
    const val imdb = "https://www.imdb.com/title/"


<<<<<<< Updated upstream
    fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw      = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                //for other device how are able to connect with Ethernet
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                //for check internet over Bluetooth
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            return nwInfo.isConnected
=======
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
>>>>>>> Stashed changes
        }
    }
}