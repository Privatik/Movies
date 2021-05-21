package com.io.movies.ui.fragment

import android.annotation.SuppressLint
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.io.movies.model.AboutMovie
import com.io.movies.model.ResultCredit
import com.io.movies.repository.AboutMovieRepository
import javax.inject.Inject

class MovieViewModel @Inject constructor(
    private val repository: AboutMovieRepository
): ViewModel() {

    val isLoadBackImage by lazy { ObservableBoolean() }

    private val _updateCredit = MutableLiveData<ResultCredit>()
    var updateCredit: LiveData<ResultCredit> = _updateCredit

    @SuppressLint("CheckResult")
    fun load(id: Int){
        repository.loadCredit(id = id).subscribe(
            {
                Log.e("ResultCredit","load - CreditResult")
                _updateCredit.postValue(it)
            },{
                Log.e("ResultCredit","error - $it")
            })
    }

    fun updateMovie(aboutMovie: AboutMovie, isFavorite: Boolean){
        repository.updateMovieFavorite(aboutMovie = aboutMovie, isFavorite = isFavorite)
    }
}