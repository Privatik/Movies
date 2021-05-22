package com.io.movies.comand

import android.util.Log
import androidx.databinding.ObservableBoolean
import com.io.movies.adapter.PagedAdapterMovie
import com.io.movies.model.Movie

class RecyclerViewListFragmentCommand(
    private val update: (Movie) -> Unit,
    private val loadAboutMovie: (Movie) -> Unit,
    private val isLoad: ObservableBoolean,
) {

    private var adapterMovie: PagedAdapterMovie? = null

    private val showMovie: (Movie) -> Unit = {
        isLoad.set(true)
        loadAboutMovie(it)
    }

    fun removeRecyclerViewAdapter(){
        adapterMovie?.currentList?.dataSource?.invalidate()
        adapterMovie = null
    }


    fun newRecyclerViewAdapter(): PagedAdapterMovie{
        if (adapterMovie != null) {
            Log.e("RecyclerView", "set null RecyclerView")
            removeRecyclerViewAdapter()
        }

        return PagedAdapterMovie(update = update, showMovie = showMovie).also { adapterMovie = it }
    }
}
