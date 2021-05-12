package com.io.movies.repository.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.io.movies.model.AboutMovie
import com.io.movies.model.Movie

@Database(entities = [Movie::class, AboutMovie::class], version = 1, exportSchema = true)
abstract class MovieDataBase: RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun aboutMovieDao(): AboutMovieDao
}