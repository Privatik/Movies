package com.io.movies.ui.fragment

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.io.movies.model.Movie
import com.io.movies.paging.MovieBoundaryCallback
import com.io.movies.paging.MovieSearchBoundaryCallback
import com.io.movies.repository.database.MovieDao
import javax.inject.Inject

class ListViewModel @Inject constructor(
        private val base: MovieDao,
        private val boundaryCallback: MovieBoundaryCallback,
        private val searchBoundaryCallback: MovieSearchBoundaryCallback,
) : ViewModel() {

    var newLists: LiveData<PagedList<Movie>>? = null

    private val config by lazy {
        PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(20)
                .build()
    }

    fun newRecycler(query: String){
        newLists = if (query.isEmpty()) LivePagedListBuilder(base.getMovieListPaging(), config).setBoundaryCallback(boundaryCallback).build()
        else {
            searchBoundaryCallback.update(query = query)
            LivePagedListBuilder(base.getMovieListPagingSearch(search = "%$query%"), config).setBoundaryCallback(searchBoundaryCallback).build()
        }

    }

    fun removeRecycler(lifecycle: LifecycleOwner) {
        newLists?.removeObservers(lifecycle)
        newLists = null
    }
}