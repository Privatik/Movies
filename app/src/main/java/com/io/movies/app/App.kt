package com.io.movies.app

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.io.movies.di.component.DaggerMainComponent
import com.io.movies.di.component.MainComponent
import com.io.movies.di.module.ContextModule
import com.io.movies.util.Config

class App: Application() {
    companion object{
        lateinit var appComponent: MainComponent
    }

    override fun onCreate() {
        super.onCreate()

       appComponent = DaggerMainComponent.builder()
           .contextModule(ContextModule(this))
           .build()
    }
}