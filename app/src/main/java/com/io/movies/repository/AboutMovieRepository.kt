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

    fun updateMovieFavorite(aboutMovie: AboutMovie, isFavorite: Boolean) = database.updateListFavorite(aboutMovie = aboutMovie, isFavorite = isFavorite)

    fun loadMovie(id: Int): Single<AboutMovie> = aboutMovieService.getMovie(movieId = id)
        .flatMap { movie ->
            database.insert(aboutMovie = movie)
            Single.just(movie)
        }
        .onErrorResumeNext( database.getMovie(id = id) )
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun loadCredit(id: Int): Single<ResultCredit> = aboutMovieService.getCredits(movieId = id)
        .flatMap { credit ->
            database.insert(credit = credit)
            Single.just(credit)
        }
        .onErrorResumeNext( database.getCredit(id = id))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}