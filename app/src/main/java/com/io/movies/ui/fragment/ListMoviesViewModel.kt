package com.io.movies.ui.fragment

import android.annotation.SuppressLint
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.io.movies.comand.FavoriteCommand
import com.io.movies.model.AboutMovie
import com.io.movies.model.Movie
import com.io.movies.repository.AboutMovieRepository
import com.io.movies.repository.MovieRepository
import javax.inject.Inject

class ListMoviesViewModel @Inject constructor(
        private val movieRepository: MovieRepository,
        private val aboutMovieRepository: AboutMovieRepository
) : ViewModel() {

    var newLists: LiveData<PagedList<Movie>>? = null

    var scrollY: Int = 0

    private val _isFavoriteMode: MutableLiveData<Boolean> = MutableLiveData()
    val isFavoriteMode: LiveData<Boolean> = _isFavoriteMode

    private val _query: MutableLiveData<String> = MutableLiveData("")
    val query: LiveData<String> = _query

    private val _loadAboutMovie: MutableLiveData<Pair<AboutMovie,Boolean>>  = MutableLiveData()
    var loadAboutMovie = _loadAboutMovie

    val isRefreshing by lazy { ObservableBoolean() }
    val isLoadAboutMovie by lazy { ObservableBoolean() }

    private val favoriteCommand by lazy{
        FavoriteCommand()
    }

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

    //Обновление LIveData
    fun postFavorite(isFavoriteMode: Boolean?){
        _isFavoriteMode.postValue(isFavoriteMode)
    }

    fun postQuery(query: String){
        movieRepository.updateQuery(query = query)
        _query.postValue(query)
    }

    //Обновление списка фаворитов
    fun updateFavoriteStateMovie(movie: Movie){
        if (isFavoriteMode.value != null && isFavoriteMode.value!!) updateMovie(movie = movie)
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
        if (isFavoriteMode.value != null && isFavoriteMode.value!!){
            onFavoriteMode()
        } else {
            offFavoriteMode()
        }
    }

    private fun onFavoriteMode(){
        movieRepository.clear()

        newLists = LivePagedListBuilder(movieRepository.factoryFavorite(query = query.value!!), config)
            .build()
    }

    private fun offFavoriteMode(){
        movieRepository.clear()

        newLists = LivePagedListBuilder(movieRepository.factoryMovieInfo(query = query.value!!), config)
            .setBoundaryCallback(movieRepository.boundaryCallback)
            .build()

        Log.e("FavoriteMode","off")
    }

    //Манипуляция с базами
    @SuppressLint("CheckResult")
    fun refresh(){
        movieRepository.refresh()
        deleteBase()
    }

    @SuppressLint("CheckResult")
    fun deleteBase() {
        movieRepository.delete()
    }

    //Загрузка детальной инофрмации
    @SuppressLint("CheckResult")
    fun load(movie: Movie){
        Log.e("AboutMovie","Load from movie with = $movie")
        aboutMovieRepository.loadMovie(id = movie.id).subscribe(
            {
                Log.e("AboutMovie","load - AboutMovie $it")
                _loadAboutMovie.postValue(Pair(it, movie.isFavorite))
            },{
                Log.e("AboutMovie","error - $it")
            })
    }

    fun setNullParamsLiveDatas() {
        _loadAboutMovie.postValue(null)
    }

    override fun onCleared() {
        movieRepository.clear()
        super.onCleared()
    }
}