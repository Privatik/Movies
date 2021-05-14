package com.io.movies.paging

import android.util.Log
import androidx.paging.PagedList
import com.io.movies.model.Movie
import com.io.movies.repository.MovieRepository
import javax.inject.Inject

class MovieBoundaryCallback constructor(private val load: (Int, String) -> Unit): PagedList.BoundaryCallback<Movie>(){

    private var count = 1
    private var query = ""

    override fun onZeroItemsLoaded() {
        Log.e("TAG","Init")
        postQuery(page = count)
    }

    override fun onItemAtEndLoaded(itemAtEnd: Movie) {
        Log.e("TAG","load new page")
        postQuery(page = count)
    }

    private fun postQuery(page: Int){
        load(page,query)
        count++
    }

    fun update(query: String){
        resetCounter()
        this.query = query
    }

    private fun resetCounter(){
        count = 1
    }

    fun refresh(){
        resetCounter()
    }
}