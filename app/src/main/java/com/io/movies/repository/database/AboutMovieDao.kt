package com.io.movies.repository.database

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.io.movies.model.AboutMovie
import com.io.movies.model.Movie
import io.reactivex.Single

@Dao
abstract class AboutMovieDao {

    @Query("SELECT * FROM aboutmovie WHERE id = :id")
    abstract fun getMovie(id: Int): Single<AboutMovie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(aboutMovie: AboutMovie)
}