package com.io.movies.repository.database

import androidx.paging.DataSource
import androidx.room.*
import com.io.movies.model.AboutMovie
import com.io.movies.model.Credit
import com.io.movies.model.Movie
import com.io.movies.model.ResultCredit
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface AboutMovieDao {

    @Query("SELECT * FROM info_about_movie WHERE id = :id")
    fun getMovie(id: Int): Single<AboutMovie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(aboutMovie: AboutMovie)

    @Query("SELECT * FROM result_credit WHERE id = :id")
    fun getCredit(id: Int): Single<ResultCredit>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(credit: ResultCredit)

}