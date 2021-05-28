package com.io.movies.repository.database

import androidx.paging.DataSource
import androidx.room.*
import com.io.movies.model.*
import io.reactivex.Single


@Dao
interface MovieDao {

    @Query("SELECT * FROM movie_info")
    fun getMovieListPaging(): DataSource.Factory<Int, MovieInfo>

    @Query("SELECT * FROM movie_info WHERE LOWER(title) LIKE LOWER(:search)")
    fun getMovieListPagingSearch(search: String): DataSource.Factory<Int, MovieInfo>

    @Query("SELECT * FROM favorite ORDER BY `order` DESC")
    fun getMovieListPagingFavorite(): DataSource.Factory<Int, Favorite>

    @Query("SELECT * FROM favorite WHERE LOWER(title) LIKE LOWER(:search) ORDER BY `order` DESC")
    fun getMovieListPagingFavoriteSearch(search: String): DataSource.Factory<Int, Favorite>

    @Query("SELECT id FROM favorite WHERE id = :id")
    fun getMovieFavorite(id: Int): Int?

    @Query("SELECT * FROM info_about_movie WHERE id = :id")
    fun getMovie(id: Int): Single<AboutMovie>

    @Query("SELECT * FROM result_credit WHERE id = :id")
    fun getCredit(id: Int): Single<ResultCredit>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insert(movieInfo: List<MovieInfo>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movieInfo: MovieInfo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(credit: ResultCredit)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favorite: Favorite)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(aboutMovie: AboutMovie)

    @Transaction
    @JvmSuppressWildcards
    fun insertOrReplace(movieInfo: MovieInfo){
        getMovieFavorite(movieInfo.id)?.let {
            movieInfo.isFavorite = true
        }
        insert(movieInfo)
    }

    @Query("UPDATE movie_info SET isFavorite = :isFavorite WHERE id = :id")
    fun updateMovie(id: Int, isFavorite: Boolean)

    @Transaction
    fun updateListFavorite(movie: Movie){
        movie.convertToMovieInfo().apply {
            updateMovie(id = id, isFavorite = isFavorite)
        }
        (movie.convertToFavorite()).let {
            if (movie.isFavorite) insert(it) else deleteFavorite(id = it.id)
        }
    }

    @Transaction
    fun updateListFavorite(aboutMovie: AboutMovie, isFavorite: Boolean){
        updateMovie(id = aboutMovie.id, isFavorite = isFavorite)
            aboutMovie.convertToFavorite().let {
            if (isFavorite) insert(it) else deleteFavorite(id = it.id)
        }
    }

    @Query("DELETE FROM movie_info")
    fun deleteMovie()

    @Query("DELETE FROM favorite WHERE id = :id")
    fun deleteFavorite(id: Int)

    @Query( "DELETE FROM info_about_movie WHERE id NOT IN (SELECT id FROM favorite)")
    fun deleteAboutMovie()

    @Query( "DELETE FROM result_credit WHERE id NOT IN (SELECT id FROM favorite)")
    fun deleteCredits()

    @Transaction
    fun delete(){
        deleteMovie()
        deleteAboutMovie()
        deleteCredits()
    }

}