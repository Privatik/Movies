package com.io.movies.paging

import android.util.Log
import androidx.paging.PagedList
import com.io.movies.model.Movie
import com.io.movies.repository.MovieRepository
import javax.inject.Inject

class MovieSearchBoundaryCallback @Inject constructor(private var movieRepository: MovieRepository): PagedList.BoundaryCallback<Movie>(){

    private var count = 1
    private var query = ""

    override fun onZeroItemsLoaded() {
        Log.e("TAg","Search init")
        movieRepository.load(page = count, search = query)
    }

    override fun onItemAtEndLoaded(itemAtEnd: Movie) {
        Log.e("TAg","Search load")
        count += 1
        movieRepository.load(page = count, search = query)
    }

    fun update(query: String) {
        count = 1
        this.query = query
    }
}