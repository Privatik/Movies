package com.io.movies.paging

import android.util.Log
import androidx.paging.PagedList
import com.io.movies.model.Movie
import com.io.movies.repository.MovieRepository
import javax.inject.Inject

class MovieBoundaryCallback @Inject constructor(private val movieRepository: MovieRepository): PagedList.BoundaryCallback<Movie>(){

    private var count = 1

    override fun onZeroItemsLoaded() {
        movieRepository.load(page = count)
    }

    override fun onItemAtEndLoaded(itemAtEnd: Movie) {
        Log.e("TAG","load new page")
        count += 1
        movieRepository.load(page = count)
    }
}