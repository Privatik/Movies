package com.io.movies.ui.dialog

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.io.movies.model.AboutMovie
import com.io.movies.repository.AboutMovieRepository
import javax.inject.Inject

class LoadViewModel @Inject constructor(
    private val repository: AboutMovieRepository
): ViewModel() {

    private val _updateMovie = MutableLiveData<AboutMovie>()
    var updateMovie = _updateMovie

    @SuppressLint("CheckResult")
    fun load(id: Int){
        Log.e("AboutMovie","Load from movie with id = $id")
        repository.loadMovie(id = id).subscribe(
            {
                Log.e("AboutMovie","load - AboutMovie $it")
                _updateMovie.postValue(it)
            },{
                Log.e("AboutMovie","error - $it")
            })
    }
}