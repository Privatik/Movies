package com.io.movies.repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.io.movies.model.Movie
import com.io.movies.model.ResultMovie
import com.io.movies.paging.MovieBoundaryCallback
import com.io.movies.repository.database.MovieDao
import com.io.movies.repository.network.MovieService
import com.io.movies.util.Config
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val movieService: MovieService,
    private val dao: MovieDao
) {

    private lateinit var isRefreshing: ObservableBoolean
    private var disposable: Disposable? = null

    val query = MutableLiveData("")

    private val config by lazy {
        PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(20)
            .setInitialLoadSizeHint(20)
            .build()
    }


    val boundaryCallback by lazy {
        val listenerQueue: (Int) -> Unit = { page -> load(page = page) }

        MovieBoundaryCallback(listenerQueue)
    }

    private val movieFactory by lazy {
        object :  DataSource.Factory<Int, Movie>() {
        override fun create(): DataSource<Int, Movie> =
            dao.getMovieListPagingSearch("%${query.value!!}%").map { it as Movie }.create()
        }
    }

    private val favouriteFactory by lazy {
        object : DataSource.Factory<Int, Movie>() {
            override fun create(): DataSource<Int, Movie> =
                dao.getMovieListPagingFavoriteSearch("%${query.value!!}%").map { it as Movie }.create()
        }
    }

    val liveDataMovieInfo: LiveData<PagedList<Movie>> by lazy {
        movieFactory.toLiveData(config = config, boundaryCallback = boundaryCallback)
    }

    val liveDataFavorite: LiveData<PagedList<Movie>> by lazy {
        favouriteFactory.toLiveData(config = config)
    }

    fun postParameters(refreshing: ObservableBoolean) {
        isRefreshing = refreshing
    }

    @SuppressLint("CheckResult")
    fun load(page: Int) {
        Log.e("TAG", "start load in search page:= $page")

        query.value?.let {
            if (it.isNotEmpty())
                loadInNetwork(movieService.getSearchMovies(page = page, query = it))
            else {
                loadInNetwork(movieService.getMovies(page = page))
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun loadInNetwork(observer: Single<ResultMovie>) {
        isRefreshing.set(true)

        disposable = observer.subscribeOn(Schedulers.single())
            .subscribe({
                if (it.page == 1){
                    if (Config.isConnect!!) delete()
                }

                it.movieInfos.forEach { movie ->
                    dao.insertOrReplace(movieInfo = movie)
                }
                isRefreshing.set(false)
                Log.e("TAG", "End load - ${it.page}")
            }, {
                isRefreshing.set(false)
                Log.e("Paging", "Repository method load: ${it.message}")
            })
    }

    @SuppressLint("CheckResult")
    fun delete() = dao.delete()

    @SuppressLint("CheckResult")
    fun updateMovie(movie: Movie) = dao.updateListFavorite(movie = movie)

    fun updateQuery() {
        boundaryCallback.update()
    }

    fun setCount(){
        val count: Int = (dao.getCountMovies() * 0.05).toInt() + 2
        Log.e("Count","$count")

        boundaryCallback.setCount(count = count)
    }

    fun invalidate(){
        liveDataMovieInfo.value?.dataSource?.invalidate()
        liveDataFavorite.value?.dataSource?.invalidate()
    }

    fun refresh() {
        boundaryCallback.refresh()
    }

    fun clear() {
        Log.e("request", "cancel")
        disposable?.dispose()
    }
}