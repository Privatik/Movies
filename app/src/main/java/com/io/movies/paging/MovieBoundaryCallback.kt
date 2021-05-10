package com.io.movies.paging

import android.util.Log
import androidx.paging.PagedList
import com.io.movies.model.Movie
import com.io.movies.repository.MovieRepository
import javax.inject.Inject

class MovieBoundaryCallback @Inject constructor(val movieRepository: MovieRepository): PagedList.BoundaryCallback<Movie>(){

    private var count = 1
    private var query = ""

    override fun onZeroItemsLoaded() {
        Log.e("TAG","Init")
        postQuery()
        count += 1
    }

    override fun onItemAtEndLoaded(itemAtEnd: Movie) {
        Log.e("TAG","load new page")
        postQuery()
        count += 1
    }

    private fun postQuery(){
        if (query == ""){
            movieRepository.load(page = count)
        } else {
            movieRepository.load(page = count, search = query)
        }
    }

    fun update(query: String){
        count = 1
        this.query = query
    }
}