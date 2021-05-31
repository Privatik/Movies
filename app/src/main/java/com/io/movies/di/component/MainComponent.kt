package com.io.movies.di.component

import com.io.movies.di.module.ContextModule
import com.io.movies.di.module.DataBaseModule
import com.io.movies.di.module.NetworkModule
import com.io.movies.ui.fragment.BaseListMoviesFragment
import com.io.movies.ui.fragment.BaseMovieFragment
import com.io.movies.viewmodelfactory.ViewModelModule
import dagger.Component
import javax.inject.Singleton

@Component(modules = [NetworkModule::class, ViewModelModule::class, DataBaseModule::class, ContextModule::class])
@Singleton
interface MainComponent {

    fun inject(fragment: BaseListMoviesFragment)
    fun inject(fragment: BaseMovieFragment)
}