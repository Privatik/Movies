package com.io.movies.repository.database

import androidx.paging.DataSource
import androidx.room.*

import com.io.movies.model.Movie
import io.reactivex.Completable
import io.reactivex.Single


@Dao
abstract class MovieDao {

    @Query("SELECT * FROM movie WHERE `like` = 0")
    abstract fun getMovieListPaging(): DataSource.Factory<Int, Movie>

    @Query("SELECT * FROM movie WHERE `like` = 0 AND title LIKE :search")
    abstract fun getMovieListPagingSearch(search: String): DataSource.Factory<Int, Movie>

    @Query("SELECT * FROM movie WHERE `like` = 1")
    abstract fun getMovieListPagingLike(): DataSource.Factory<Int, Movie>

    @Query("SELECT * FROM movie WHERE `like` = 1 AND title LIKE :search ")
    abstract fun getMovieListPagingLikeSearch(search: String): DataSource.Factory<Int, Movie>

    @Query("SELECT * FROM movie WHERE id = :id And `like` = 1")
    abstract fun getMovieLike(id: Int): Movie?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    abstract fun insert(movie: List<Movie>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(movie: Movie)

    @Update
    abstract fun update(movie: Movie): Completable

    @Query( "DELETE FROM movie WHERE `like` = 0")
    abstract fun delete(): Completable

    @Transaction
    @JvmSuppressWildcards
    open fun insertOrReplace(movie: Movie){
         getMovieLike(movie.id)?.let {
             movie.like = it.like
         }
        insert(movie)
    }
}