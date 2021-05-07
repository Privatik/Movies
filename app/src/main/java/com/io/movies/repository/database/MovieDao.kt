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

    @Query("SELECT * FROM movie WHERE title LIKE :search")
    fun getMovieListPagingSearch(search: String): DataSource.Factory<Int, Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insert(movie: List<Movie>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: Movie)

    @Delete
    fun delete(movie: Movie): Completable
}