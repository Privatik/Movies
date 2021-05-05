package com.io.movies.ui.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.io.movies.model.Movie
import com.io.movies.paging.MovieBoundaryCallback
import com.io.movies.repository.database.MovieDataBase
import javax.inject.Inject

class ListViewModel @Inject constructor(
        dataBase: MovieDataBase,
        boundaryCallback: MovieBoundaryCallback) : ViewModel() {

    var newsList: LiveData<PagedList<Movie>>

    init {
        val config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(20)
                .build()
        newsList = LivePagedListBuilder(dataBase.movieDao().getMovieListPaging(), config).setBoundaryCallback(boundaryCallback).build()
    }
}