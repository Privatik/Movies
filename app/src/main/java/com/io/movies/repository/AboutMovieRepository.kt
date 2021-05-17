package com.io.movies.repository

import com.io.movies.model.AboutMovie
import com.io.movies.model.ResultCredit
import com.io.movies.repository.database.MovieDao
import com.io.movies.repository.network.MovieService
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AboutMovieRepository @Inject constructor(
    private val aboutMovieService: MovieService,
    private val database: MovieDao
) {

    fun updateBase(aboutMovie: AboutMovie){
         database.insert(aboutMovie = aboutMovie)
    }

    fun updateBase(credit: ResultCredit){
         database.insert(credit = credit)
    }

    fun updateMovieFavorite(aboutMovie: AboutMovie, isFavorite: Boolean) = database.updateListFavorite(aboutMovie = aboutMovie, isFavorite = isFavorite)

    fun loadMovie(id: Int): Single<AboutMovie> = database.getMovie(id = id)
            .onErrorResumeNext( aboutMovieService.getMovie(movieId = id) )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun loadCredit(id: Int): Single<ResultCredit> = database.getCredit(id = id)
        .onErrorResumeNext( aboutMovieService.getCredits(movieId = id))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}