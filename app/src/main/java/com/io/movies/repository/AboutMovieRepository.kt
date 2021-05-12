package com.io.movies.repository

import com.io.movies.model.AboutMovie
import com.io.movies.repository.database.AboutMovieDao
import com.io.movies.repository.network.AboutMovieService
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AboutMovieRepository @Inject constructor(
    private val aboutMovieService: AboutMovieService,
    private val database: AboutMovieDao
) {


    fun load(id: Int): Observable<AboutMovie> {
        val remove = database.getMovie(id = id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .toObservable()
            .flatMap {
               Observable.just(it)
            }

        val locale = aboutMovieService.getMovie(movieId = id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .toObservable()
            .flatMap {
                database.insert(it)
                Observable.just(it)
            }

        return Observable.concat(remove, locale)
    }
}