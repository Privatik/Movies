package com.io.movies.repository

import android.annotation.SuppressLint
import android.util.Log
import com.io.movies.repository.database.MovieDataBase
import com.io.movies.repository.network.MovieService
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val movieService: MovieService,
    private val database: MovieDataBase
) {

    @SuppressLint("CheckResult")
    fun load(page: Int){
        Log.e("TAG","start load page:= $page")
        movieService.getMovie(page = page)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    Log.e("TAG","save new page $it")
                    //database.movieDao().insert(it.movies)
                    it.movies.forEach { movie ->
                        database.movieDao().insert(movie)
                    }
                },{
                    Log.e("Paging","Repository method load: ${it.message}")
        })
    }
}