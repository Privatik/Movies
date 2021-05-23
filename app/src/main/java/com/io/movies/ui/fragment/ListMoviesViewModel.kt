package com.io.movies.ui.fragment

import android.annotation.SuppressLint
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.google.android.material.snackbar.Snackbar
import com.io.movies.comand.FavoriteCommand
import com.io.movies.model.Movie
import com.io.movies.repository.MovieRepository
import com.io.movies.util.Config
import javax.inject.Inject

class ListMoviesViewModel @Inject constructor(
        private val movieRepository: MovieRepository
) : ViewModel() {

    var newLists: LiveData<PagedList<Movie>>? = null

    private val _isFavoriteMode: MutableLiveData<Boolean> = MutableLiveData()
    private val _query: MutableLiveData<String> = MutableLiveData("")
    val mediatorUpdateRecyclerView = MediatorLiveData<Any>()

    private var observableConnect: Observer<Boolean>? = null

    val isRefreshing by lazy { ObservableBoolean() }
    val isLoadAboutMovie by lazy { ObservableBoolean() }

    private val favoriteCommand by lazy{
        FavoriteCommand()
    }

    private var isFirstStart = true

    private val config by lazy {
        PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(20)
            .setInitialLoadSizeHint(40)
            .build()
    }

    init {
        movieRepository.postParameters(isRefreshing)

        mediatorUpdateRecyclerView.addSource(_query){ mediatorUpdateRecyclerView.value = it}
        mediatorUpdateRecyclerView.addSource(_isFavoriteMode){ mediatorUpdateRecyclerView.value = it}
    }

    //Обновление LIveData
    fun postFavorite(isFavoriteMode: Boolean?){
        _isFavoriteMode.postValue(isFavoriteMode)
    }

    fun postQuery(query: String){
        movieRepository.updateQuery(query = query)
        _query.postValue(query)
    }

    fun getQuery():String {
        return _query.value ?: ""
    }

    fun getIsFavorite():Boolean {
        return _isFavoriteMode.value ?: false
    }

    //Обновление списка фаворитов
    fun updateFavoriteStateMovie(movie: Movie){
        if (getIsFavorite()) updateMovie(movie = movie)
            else favoriteCommand.changeFavoriteStateMovie(movie = movie)
    }

    fun updateMovies(){
        favoriteCommand.updateListFavorite().let {
            it.forEach { movie ->
                updateMovie(movie = movie)
            }
            it.clear()
        }
    }

    private fun updateMovie(movie: Movie){
        Log.e("Tag", "Update $movie")
        movieRepository.updateMovie(movie = movie)
    }

    //Замена списка (поиск, список фаворитов)
    fun updateQuery(){
        if (getIsFavorite()){
            onFavoriteMode()
        } else {
            offFavoriteMode()
        }
    }

    private fun onFavoriteMode(){
        movieRepository.clear()

        newLists = LivePagedListBuilder(movieRepository.factoryFavorite(query = getQuery()), config)
            .build()
    }

    private fun offFavoriteMode(){
        movieRepository.clear()

        newLists = LivePagedListBuilder(movieRepository.factoryMovieInfo(query = getQuery()), config)
            .setBoundaryCallback(movieRepository.boundaryCallback)
            .build()

        Log.e("FavoriteMode","off")
    }

    //Контроль сети
    fun isConnect(liveDataConnect: LiveData<Boolean>,
                  isNotConnect: () -> Unit,
                  updateLoad: () -> Unit,
                  firstStart: () -> Unit){

        if(observableConnect == null) {
            observableConnect = Observer {
                if (isFirstStart) {
                    if (it) deleteBase()
                    else isNotConnect()
                    isFirstStart = false
                    firstStart()
                }
                else {
                    if (it){
                        movieRepository.boundaryCallback.newConnectNetwork()
                        updateLoad()
                    }
                    else {
                        isNotConnect()
                    }
                }
                Log.e("Connect", "new connect $it")
            }
        }

        liveDataConnect.observeForever(observableConnect!!)
    }

    //Манипуляция с базами
    @SuppressLint("CheckResult")
    fun refresh(){
        movieRepository.boundaryCallback.refresh()
        deleteBase()
    }

    @SuppressLint("CheckResult")
    fun deleteBase() {
        movieRepository.delete()
    }

    override fun onCleared() {
        observableConnect?.let { Config.isOnline()?.removeObserver(it) }
        movieRepository.clear()
        super.onCleared()
    }
}