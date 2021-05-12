package com.io.movies.paging

import android.util.Log
import androidx.paging.PagedList
import com.io.movies.model.Movie
import com.io.movies.repository.MovieRepository
import javax.inject.Inject

class MovieBoundaryCallback @Inject constructor(val movieRepository: MovieRepository): PagedList.BoundaryCallback<Movie>(){

    private var count = 2
    private var query = ""

    override fun onZeroItemsLoaded() {
        Log.e("TAG","Init")
        postQuery(page = 1)
    }

    override fun onItemAtEndLoaded(itemAtEnd: Movie) {
        Log.e("TAG","load new page")
        postQuery(page = count)
        count++
    }

    private fun postQuery(page: Int){
        if (query == ""){
            movieRepository.load(page = page)
        } else {
            movieRepository.load(page = page, search = query)
        }
    }

    fun update(query: String){
        count = 2
        this.query = query
    }

    fun refresh(){
        count = 2
    }
}