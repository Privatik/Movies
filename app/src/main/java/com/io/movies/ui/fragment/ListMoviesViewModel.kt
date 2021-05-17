package com.io.movies.ui.fragment

import android.annotation.SuppressLint
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.io.movies.model.Movie
import com.io.movies.repository.MovieRepository
import javax.inject.Inject

class ListMoviesViewModel @Inject constructor(
        private val movieRepository: MovieRepository
) : ViewModel() {

    var newLists: LiveData<PagedList<Movie>>? = null
    var isFavoriteMode = false
    var isConnect: Boolean = true
    val isRefreshing by lazy { ObservableBoolean() }
    var query = ""

    private val config by lazy {
        PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(20)
            .setInitialLoadSizeHint(40)
            .build()
    }

    init {
        movieRepository.postParameters(isRefreshing)
    }

    fun updateMovies(movies: List<Movie>){
        movies.forEach {
            updateMovie(movie = it)
        }
    }

    fun updateMovie(movie: Movie){
        Log.e("Tag", "Update $movie")
        movieRepository.updateMovie(movie = movie)
    }

    fun setNullLiveData(){
        newLists = null
    }

    fun getLifeData(): LiveData<PagedList<Movie>> {
        if (newLists == null) {
            newLIfeData()
        }
        return newLists!!
    }

    private fun newLIfeData(){
        val live = LivePagedListBuilder(movieRepository.factory(query = query, isFavoriteMode = isFavoriteMode), config)

        if (!isFavoriteMode && isConnect) {
            movieRepository.updateQuery(query = query)
            live.setBoundaryCallback(
                movieRepository.callback()
            )
        }
        newLists = live.build()
    }

    @SuppressLint("CheckResult")
    fun refresh(){
        if (isConnect) {
            movieRepository.refresh()
            deleteBase()
        }
    }

    @SuppressLint("CheckResult")
    fun deleteBase() {
        movieRepository.delete()
    }
}