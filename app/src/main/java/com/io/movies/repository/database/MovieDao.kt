package com.io.movies.repository.database

import androidx.paging.DataSource
import androidx.room.*

import com.io.movies.model.Movie
import io.reactivex.Completable
import io.reactivex.Single


@Dao
interface MovieDao {

    @Query("SELECT * FROM movie")
    fun getMovieListPaging(): DataSource.Factory<Int, Movie>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    @JvmSuppressWildcards
    fun insert(movie: List<Movie>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(movie: Movie)

    @Delete
    fun delete(movie: Movie): Completable
}