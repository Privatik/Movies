package com.io.movies.repository.database

import androidx.paging.DataSource
import androidx.room.*

import com.io.movies.model.Movie
import io.reactivex.Completable
import io.reactivex.Single


@Dao
interface MovieDao {

    @Query("SELECT * FROM movie WHERE `like` = 0")
    fun getMovieListPaging(): DataSource.Factory<Int, Movie>

    @Query("SELECT * FROM movie WHERE `like` = 0 AND title LIKE :search")
    fun getMovieListPagingSearch(search: String): DataSource.Factory<Int, Movie>

    @Query("SELECT * FROM movie WHERE `like` = 1")
    fun getMovieListPagingLike(): DataSource.Factory<Int, Movie>

    @Query("SELECT * FROM movie WHERE `like` = 1 AND title LIKE :search")
    fun getMovieListPagingLikeSearch(search: String): DataSource.Factory<Int, Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insert(movie: List<Movie>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: Movie)

    @Update
    fun update(movie: Movie): Completable

    @Query( "DELETE FROM movie")
    fun delete(): Completable
}