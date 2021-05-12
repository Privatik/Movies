package com.io.movies.ui.fragment

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.io.movies.model.Movie
import com.io.movies.paging.MovieBoundaryCallback
import javax.inject.Inject

class ListViewModel @Inject constructor(
        private val boundaryCallback: MovieBoundaryCallback
) : ViewModel() {

    var newLists: LiveData<PagedList<Movie>>? = null
    var isFavoriteMode = false
    var isConnect: Boolean = true

    private val config by lazy {
        PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(20)
            .setInitialLoadSizeHint(40)
            .build()
    }

    @SuppressLint("CheckResult")
    fun updateMovie(movie: Movie){
        boundaryCallback.movieRepository.update(movie = movie)
    }

    fun newRecycler(query: String){
        val live = LivePagedListBuilder(boundaryCallback.movieRepository.factory(query = query, isFavoriteMode = isFavoriteMode), config)

        if (!isFavoriteMode && isConnect) {
            boundaryCallback.update(query = query)
            live.setBoundaryCallback(
                boundaryCallback
            )
        }
        newLists = live.build()
    }

    @SuppressLint("CheckResult")
    fun refresh(){
        if (isConnect) {
            boundaryCallback.refresh()
            deleteBase()
        }
    }

    @SuppressLint("CheckResult")
    fun deleteBase() {
        boundaryCallback.movieRepository.delete()
    }
}