package com.io.movies.di.module

import android.content.Context
import androidx.room.Room
import com.io.movies.repository.database.MovieDao
import com.io.movies.repository.database.MovieDataBase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ContextModule::class])
class DataBaseModule {

    @Provides
    @Singleton
    fun movieDataBase(context: Context): MovieDataBase =
        Room.databaseBuilder(
            context,
            MovieDataBase::class.java,
            "movie_database"
        ).allowMainThreadQueries()
                .build()

    @Provides
    @Singleton
    fun daoMovie(database: MovieDataBase): MovieDao = database.movieDao()
}