package com.io.movies.repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.paging.DataSource
import com.io.movies.model.Movie
import com.io.movies.model.ResultMovie
import com.io.movies.paging.MovieBoundaryCallback
import com.io.movies.repository.database.MovieDao
import com.io.movies.repository.network.MovieService
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val movieService: MovieService,
    private val database: MovieDao
) {

    private lateinit var isRefreshing: ObservableBoolean

    private val boundaryCallback by lazy {
        val listenerQueue: (Int, String) -> Unit = { page, query ->
            if (query.isEmpty()) {
                load(page = page)
            } else {
                load(page = page, query = query)
            }
        }

        MovieBoundaryCallback(listenerQueue)
    }

    fun postParameters(refreshing: ObservableBoolean) {
        isRefreshing = refreshing
    }

    fun callback(): MovieBoundaryCallback = boundaryCallback

    @SuppressLint("CheckResult")
    fun load(page: Int) {
        Log.e("TAG", "start load in main page:= $page")

        loadInNetwork(movieService.getMovies(page = page))
    }

    @SuppressLint("CheckResult")
    fun load(page: Int, query: String) {
        Log.e("TAG", "start load in search page:= $page $query")

        loadInNetwork(movieService.getSearchMovies(page = page, query = query))
    }

    @SuppressLint("CheckResult")
    fun delete() = database.delete()

    fun factory(query: String, isFavoriteMode: Boolean = false): DataSource.Factory<Int, Movie> {
        return if (isFavoriteMode) factoryFavorite(query = query)
        else factoryMovieInfo(query = query)
    }

    private fun factoryMovieInfo(query: String): DataSource.Factory<Int, Movie> =
        if (query.isEmpty()) {
            database.getMovieListPaging().map{ it as Movie}
        } else {
            database.getMovieListPagingSearch(search = "%$query%").map{ it as Movie}
        }

    private fun factoryFavorite(query: String): DataSource.Factory<Int, Movie> =
        if (query.isEmpty()) {
            database.getMovieListPagingFavorite().map{ it as Movie}
        } else {
            database.getMovieListPagingFavoriteSearch(search = "%$query%").map{ it as Movie}
        }


    @SuppressLint("CheckResult")
    private fun loadInNetwork(observer: Single<ResultMovie>){
        isRefreshing.set(true)

        observer.subscribeOn(Schedulers.io())
                //.observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                  //  Log.e("TAG","save new page $it")
                    //database.movieDao().insert(it.movies)
                    it.movieInfos.forEach { movie ->
                        //Log.e("Save Movie","$movie")
                       // database.insert(movie = movie)
                       database.insertOrReplace(movieInfo = movie)
                    }
                    isRefreshing.set(false)
                    Log.e("TAG","End load")
                },{
                    Log.e("Paging","Repository method load: ${it.message}")
                })
    }

    @SuppressLint("CheckResult")
    fun updateMovie(movie: Movie) = database.updateListFavorite(movie = movie)

    fun updateQuery(query: String = ""){
        boundaryCallback.update(query = query)
    }

    fun refresh(){
        boundaryCallback.refresh()
    }
}