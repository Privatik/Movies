package com.io.movies.ui.fragment

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.io.movies.model.AboutMovie
import com.io.movies.repository.AboutMovieRepository
import javax.inject.Inject

class MovieViewModel @Inject constructor(
    private val repository: AboutMovieRepository
): ViewModel() {

    private val _updateMovie = MutableLiveData<AboutMovie>()
    var updateMovie = _updateMovie

    @SuppressLint("CheckResult")
    fun load(id: Int){
        repository.load(id = id).subscribe({
            Log.e("AboutMovie", "Load $it")
            _updateMovie.postValue(it)
        }, {
            Log.e("AboutMovie", "error $it")
        })
    }
}