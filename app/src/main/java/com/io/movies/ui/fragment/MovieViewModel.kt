package com.io.movies.ui.fragment

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.io.movies.model.AboutMovie
import com.io.movies.model.ResultCredit
import com.io.movies.repository.AboutMovieRepository
import javax.inject.Inject

class MovieViewModel @Inject constructor(
    private val repository: AboutMovieRepository
): ViewModel() {

    private val _updateMovie = MutableLiveData<AboutMovie>()
    var updateMovie = _updateMovie

    private val _updateCredit = MutableLiveData<ResultCredit>()
    var updateCredit = _updateCredit

    @SuppressLint("CheckResult")
    fun load(id: Int){
        Log.e("AboutMovie","Load from movie with id = $id")
        repository.loadMovie(id = id).subscribe(
            {
            Log.e("AboutMovie","load - AboutMovi")
                _updateMovie.postValue(it)
                repository.updateBase(it)
        },{
            Log.e("AboutMovie","erroe - $it")
        })

        repository.loadCredit(id = id).subscribe(
            {
                Log.e("ResultCredit","load - CreditResult")
                _updateCredit.postValue(it)
                repository.updateBase(it)
            },{
                Log.e("ResultCredit","erroe - $it")
            })
    }
}