package com.io.movies.ui.fragment

import android.annotation.SuppressLint
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.paging.DataSource
import com.io.movies.comand.FavoriteCommand
import com.io.movies.model.Movie
import com.io.movies.repository.MovieRepository
import com.io.movies.repository.database.MovieDao
import com.io.movies.util.Config
import javax.inject.Inject

class ListMoviesViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _isFavoriteModeMutable: MutableLiveData<Boolean> = MutableLiveData(false)
    val isFavoriteMode: LiveData<Boolean> = _isFavoriteModeMutable

    private val _queryMutable: MutableLiveData<String> by lazy { movieRepository.query }
    val query: LiveData<String> = _queryMutable

    val isRefreshing by lazy { ObservableBoolean() }
    val isLoadMovieFragment by lazy { ObservableBoolean() }

    val livedataMovieInfo by lazy { movieRepository.liveDataMovieInfo }
    val livedataFavorite by lazy { movieRepository.liveDataFavorite }

    private val observerQuery = Observer<String> {
        updateQuery()
    }

    private val observerFavorite = Observer<Boolean> {
        updateListFavoriteMovie()
    }


    private val favoriteCommand by lazy {
        FavoriteCommand()
    }

    var isFirstStart = true

    init {
        movieRepository.postParameters(isRefreshing)
    }

    fun getIsFavorite(): Boolean {
        return isFavoriteMode.value ?: false
    }

    fun postQuery(query: String){
        _queryMutable.postValue(query)
    }

    fun postFavorite(isFavorite: Boolean){
        _isFavoriteModeMutable.postValue(isFavorite)
    }

    fun load() {
        query.observeForever(observerQuery)
        isFavoriteMode.observeForever(observerFavorite)
    }

    //Обновление списка фаворитов
    fun updateFavoriteStateMovie(movie: Movie) {
        if (getIsFavorite()) updateMovie(movie = movie)
        else favoriteCommand.changeFavoriteStateMovie(movie = movie)
    }

    fun updateListFavoriteMovie() {
        favoriteCommand.updateListFavorite().let {
            Log.e("UpdateMovie", "Size updateMovieList - ${it.size}")
            it.forEach { movie ->
                updateMovie(movie = movie)
            }
            it.clear()
        }
    }

    private fun updateMovie(movie: Movie) {
        Log.e("Tag", "Update $movie")
        movieRepository.updateMovie(movie = movie)
    }

    //Замена списка (поиск, список фаворитов)
    private fun updateQuery() {
        updateListFavoriteMovie()
        movieRepository.clear()
        movieRepository.invalidate()

        if (Config.isConnect!!) {
            movieRepository.updateQuery()
            deleteBase()
        }
    }

    //Откат загрузки
    fun newConnectNetwork() {
        movieRepository.boundaryCallback.newConnectNetwork()
    }

    fun setCount(){
        movieRepository.setCount()
    }

    fun invalidate(){
        movieRepository.invalidate()
    }

    //Манипуляция с базами
    @SuppressLint("CheckResult")
    fun refresh() {
        movieRepository.refresh()
        deleteBase()
    }

    @SuppressLint("CheckResult")
    fun deleteBase() {
        movieRepository.delete()
    }

    fun clear() {
        movieRepository.clear()
    }

    override fun onCleared() {
        isFavoriteMode.removeObserver(observerFavorite)
        query.removeObserver(observerQuery)

        clear()
        super.onCleared()
    }
}