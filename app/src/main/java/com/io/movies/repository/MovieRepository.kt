package com.io.movies.repository

import android.annotation.SuppressLint
import android.util.Log
import io.reactivex.Single
import com.io.movies.model.Movie
import com.io.movies.model.ResultMovie
import com.io.movies.repository.database.MovieDao
import com.io.movies.repository.database.MovieDataBase
import com.io.movies.repository.network.MovieService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val movieService: MovieService,
    private val database: MovieDao
) {

    @SuppressLint("CheckResult")
    fun load(page: Int){
        Log.e("TAG","start load in main page:= $page")

        loadInNetwork(movieService.getMovies(page = page))
    }

    @SuppressLint("CheckResult")
    fun load(page: Int, search: String){
        Log.e("TAG","start load in search page:= $page $search")

        loadInNetwork(movieService.getSearchMovies(page = page, query = search))
    }

    @SuppressLint("CheckResult")
    private fun loadInNetwork(observer: Single<ResultMovie>){
        observer.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.e("TAG","save new page $it")
                    //database.movieDao().insert(it.movies)
                    it.movies.forEach {  movie ->
                       // Log.e("Save Movie","$movie")
                        database.insert(movie = movie)
                    }
                    Log.e("TAG","End load")
                },{
                    Log.e("Paging","Repository method load: ${it.message}")
                })
    }}