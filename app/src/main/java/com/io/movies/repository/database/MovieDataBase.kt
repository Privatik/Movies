package com.io.movies.repository.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.io.movies.model.AboutMovie
import com.io.movies.model.Favorite
import com.io.movies.model.MovieInfo
import com.io.movies.model.ResultCredit

@Database(entities = [MovieInfo::class, AboutMovie::class, ResultCredit::class, Favorite::class], version = 1, exportSchema = true)
abstract class MovieDataBase: RoomDatabase() {
    abstract fun movieDao(): MovieDao
}