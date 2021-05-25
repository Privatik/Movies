package com.io.movies.repository

import android.util.Log
import com.io.movies.model.AboutMovie
import com.io.movies.model.ResultCredit
import com.io.movies.repository.database.MovieDao
import com.io.movies.repository.network.MovieService
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AboutMovieRepository @Inject constructor(
    private val aboutMovieService: MovieService,
    private val database: MovieDao
) {

    private var disposableAboutMovie: Disposable? = null
    private var disposableResultCredit: Disposable? = null

    fun updateMovieFavorite(aboutMovie: AboutMovie, isFavorite: Boolean) {
        Log.e("UpdateMovie","title: ${aboutMovie.title} isFavorite: $isFavorite")
        database.updateListFavorite(aboutMovie = aboutMovie, isFavorite = isFavorite)
    }

    fun setDisposableAboutMovie(disposable: Disposable){
        disposableAboutMovie = disposable
    }

    fun setDisposableResultCredit(disposable: Disposable){
        disposableResultCredit = disposable
    }

    fun loadMovie(id: Int): Single<AboutMovie> = aboutMovieService.getMovie(movieId = id)
        .flatMap { movie ->
            database.insert(aboutMovie = movie)
            Single.just(movie)
        }
        .onErrorResumeNext( database.getMovie(id = id) )
        .subscribeOn(Schedulers.io())

    fun loadCredit(id: Int): Single<ResultCredit> = aboutMovieService.getCredits(movieId = id)
        .flatMap { credit ->
            database.insert(credit = credit)
            Single.just(credit)
        }
        .onErrorResumeNext( database.getCredit(id = id))
        .subscribeOn(Schedulers.io())

    fun clear(){
        disposableAboutMovie?.dispose()
        disposableResultCredit?.dispose()
    }
}