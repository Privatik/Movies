package com.io.movies.repository

import android.util.Log
import com.io.movies.model.AboutMovie
import com.io.movies.model.ResultCredit
import com.io.movies.repository.database.AboutMovieDao
import com.io.movies.repository.network.AboutMovieService
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AboutMovieRepository @Inject constructor(
    private val aboutMovieService: AboutMovieService,
    private val database: AboutMovieDao
) {

    private var isLoadedFromNetwork = false

   private fun getFromNetworkMovie(id: Int): Single<AboutMovie>{
        Log.e("Movie", "is load from network")
        isLoadedFromNetwork = true
        return aboutMovieService.getMovie(movieId = id)
    }

   private fun getFromNetworkCredit(id: Int): Single<ResultCredit>{
        Log.e("Movie", "is load from network")
       // isLoadedFromNetwork = true
        return aboutMovieService.getCredits(movieId = id)
    }

    fun updateBase(aboutMovie: AboutMovie){
        if (isLoadedFromNetwork) database.insert(aboutMovie = aboutMovie)
    }

    fun updateBase(credit: ResultCredit){
        if (isLoadedFromNetwork) database.insert(credit = credit)
    }

    fun loadMovie(id: Int): Single<AboutMovie> = database.getMovie(id = id)
            .onErrorResumeNext( getFromNetworkMovie(id = id) )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun loadCredit(id: Int): Single<ResultCredit> = database.getCredit(id = id)
        .onErrorResumeNext( getFromNetworkCredit(id = id))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}