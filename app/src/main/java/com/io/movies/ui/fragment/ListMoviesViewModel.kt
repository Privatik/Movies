package com.io.movies.ui.fragment

import android.annotation.SuppressLint
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private val _isFavoriteMode: MutableLiveData<Boolean> = MutableLiveData(false)
    val isFavoriteMode: LiveData<Boolean> = _isFavoriteMode

    private val _query: MutableLiveData<String> = MutableLiveData("")
    val query: LiveData<String> = _query

    val isRefreshing by lazy { ObservableBoolean() }

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

    fun postFavorite(isFavoriteMode: Boolean){
        _isFavoriteMode.postValue(isFavoriteMode)
    }

    fun postQuery(query: String){
        _query.postValue(query)
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
            newLifeData()
        }
        return newLists!!
    }

    private fun newLifeData(){
        val live = LivePagedListBuilder(movieRepository.factory(query = query, isFavoriteMode = isFavoriteMode), config)

        if (isFavoriteMode) {
            movieRepository.updateQuery(query = query)
            live.setBoundaryCallback(
                movieRepository.callback()
            )
        }
        newLists = live.build()
    }

    fun updateQuery(){
        if (isFavoriteMode.value!!){
            onFavoriteMode()
        } else {
            offFavoriteMode()
        }
    }

    fun onFavoriteMode(){
        newLists =  LivePagedListBuilder(movieRepository.factory(query = query.value!!), config)
            .build()
    }

    fun offFavoriteMode(){
        newLists =  LivePagedListBuilder(movieRepository.factory(query = query.value!!), config)
            .setBoundaryCallback(movieRepository.callback())
            .build()
    }

    @SuppressLint("CheckResult")
    fun refresh(){
        movieRepository.refresh()
        deleteBase()
    }

    @SuppressLint("CheckResult")
    fun deleteBase() {
        movieRepository.delete()
    }

    override fun onCleared() {
        super.onCleared()
    }
}