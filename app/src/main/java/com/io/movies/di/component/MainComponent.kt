package com.io.movies.di.component

import com.io.movies.di.module.ContextModule
import com.io.movies.di.module.DataBaseModule
import com.io.movies.di.module.NetworkModule
import com.io.movies.ui.activity.MainActivity
import com.io.movies.ui.fragment.ListFragment
import com.io.movies.viewmodelfactory.ViewModelModule
import dagger.Component
import javax.inject.Singleton

@Component(modules = [NetworkModule::class, ViewModelModule::class, DataBaseModule::class, ContextModule::class])
@Singleton
interface MainComponent {

    fun inject(fragment: ListFragment)
}