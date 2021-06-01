package com.io.movies.ui.fragment

import android.annotation.SuppressLint
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.io.movies.model.AboutMovie
import com.io.movies.model.Movie
import com.io.movies.model.ResultCredit
import com.io.movies.repository.AboutMovieRepository
import javax.inject.Inject

class MovieViewModel @Inject constructor(
    private val repository: AboutMovieRepository
): ViewModel() {

    val isLoadBackImage by lazy { ObservableBoolean() }
    val isLoadAboutMovie by lazy { ObservableBoolean(true) }

    private val _updateCredit = MutableLiveData<ResultCredit>()
    var updateCredit: LiveData<ResultCredit> = _updateCredit

    private val _loadAboutMovie: MutableLiveData<AboutMovie>  = MutableLiveData()
    var loadAboutMovie: LiveData<AboutMovie> = _loadAboutMovie

    @SuppressLint("CheckResult")
    fun load(id: Int){
        Log.e("AboutMovie","Load from movie with = $id")
        repository.loadMovie(id = id).subscribe(
            {
                Log.e("AboutMovie","load - AboutMovie $it")
                if (loadAboutMovie.value != null) return@subscribe
                _loadAboutMovie.postValue(it)
            },{
                Log.e("AboutMovie","error - $it")
            }).also { repository.setDisposableAboutMovie(it) }

        repository.loadCredit(id = id).subscribe(
            {
                Log.e("ResultCredit","load - CreditResult")
                if (loadAboutMovie.value != null) return@subscribe
                _updateCredit.postValue(it)
            },{
                Log.e("ResultCredit","error - $it")
            }).also { repository.setDisposableResultCredit(it) }
    }

    fun updateMovie(aboutMovie: AboutMovie, isFavorite: Boolean){
        repository.updateMovieFavorite(aboutMovie = aboutMovie, isFavorite = isFavorite)
    }

    fun clear(){
        repository.clear()
    }

    override fun onCleared() {
        clear()
        super.onCleared()
    }
}