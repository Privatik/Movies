package com.io.movies.paging

import android.util.Log
import androidx.paging.PagedList
import com.io.movies.model.Movie
import com.io.movies.model.MovieInfo

class MovieBoundaryCallback constructor(private val load: (Int) -> Unit): PagedList.BoundaryCallback<Movie>(){

    private var count = 1

    override fun onZeroItemsLoaded() {
        Log.e("TAG","Init")
        postQuery(page = count)
    }

    override fun onItemAtEndLoaded(itemAtEnd: Movie) {
        Log.e("TAG","load new page")
        postQuery(page = count)
    }

    private fun postQuery(page: Int){
        load(page)
        count++
    }

    fun setCount(count: Int){
        this.count = count
    }

    fun update(){
        resetCounter()
    }

    fun refresh(){
        resetCounter()
    }

    private fun resetCounter(){
        count = 1
    }

    fun newConnectNetwork(){
        if (count > 1) {
            count--
        }
    }
}