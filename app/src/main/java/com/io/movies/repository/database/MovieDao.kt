package com.io.movies.repository.database

import androidx.paging.DataSource
import androidx.room.*
import com.io.movies.model.AboutMovie

import com.io.movies.model.Movie
import com.io.movies.model.ResultCredit
import io.reactivex.Completable
import io.reactivex.Single


@Dao
interface MovieDao {

    @Query("SELECT * FROM movie WHERE isFavorite = 0")
    fun getMovieListPaging(): DataSource.Factory<Int, Movie>

    @Query("SELECT * FROM movie WHERE isFavorite = 0 AND title LIKE :search")
    fun getMovieListPagingSearch(search: String): DataSource.Factory<Int, Movie>

    @Query("SELECT * FROM movie WHERE isFavorite = 1")
    fun getMovieListPagingLike(): DataSource.Factory<Int, Movie>

    @Query("SELECT * FROM movie WHERE isFavorite = 1 AND title LIKE :search ")
    fun getMovieListPagingLikeSearch(search: String): DataSource.Factory<Int, Movie>

    @Query("SELECT * FROM movie WHERE id = :id And isFavorite = 1")
    fun getMovieLike(id: Int): Movie?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insert(movie: List<Movie>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: Movie)

    @Update
    fun updateMovie(movie: Movie): Completable

    @Query("UPDATE movie SET isFavorite = :isFavorite WHERE id = :id")
    fun updateMovie(id: Int, isFavorite: Boolean)

    @Query("DELETE FROM movie WHERE isFavorite = 0")
    fun deleteMovie()

    @Query( "DELETE FROM info_about_movie WHERE id NOT IN (SELECT id FROM movie)")
    fun deleteAboutMovie()

    @Query( "DELETE FROM result_credit WHERE id NOT IN (SELECT id FROM movie)")
    fun deleteCredits()

    @Query("SELECT * FROM info_about_movie WHERE id = :id")
    fun getMovie(id: Int): Single<AboutMovie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(aboutMovie: AboutMovie)

    @Query("SELECT * FROM result_credit WHERE id = :id")
    fun getCredit(id: Int): Single<ResultCredit>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(credit: ResultCredit)


    @Transaction
    fun delete(){
        deleteMovie()
        deleteAboutMovie()
        deleteCredits()
    }

    @Transaction
    @JvmSuppressWildcards
    fun insertOrReplace(movie: Movie){
         getMovieLike(movie.id)?.let {
             movie.isFavorite = it.isFavorite
         }
        insert(movie)
    }
}