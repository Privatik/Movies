package com.io.movies.comand

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.io.movies.adapter.PagedAdapterMovie
import com.io.movies.model.Movie

class RecyclerViewListFragmentCommand(
    private val update: (Movie) -> Unit,
    private val showMovie: (Movie) -> Unit,
    private val liveData: () -> LiveData<PagedList<Movie>>,
    private val setNullLiveData:() -> Unit) {

    lateinit var lifeCycleOwner: LifecycleOwner
    private var adapterMovie: PagedAdapterMovie? = null

    private fun newRecyclerViewAdapter(): PagedAdapterMovie =
        if (adapterMovie == null) {
            PagedAdapterMovie(update = update, showMovie = showMovie)
                .also {
                    adapterMovie = it
                }
        }
        else adapterMovie!!

    private fun removeRecyclerViewAdapter(){
        liveData().removeObservers(lifeCycleOwner)
        adapterMovie?.currentList?.dataSource?.invalidate()
        adapterMovie = null
    }


    fun newRecycler(isRestart: Boolean): PagedAdapterMovie{
        if (isRestart){
            Log.e("RecyclerView","set null RecyclerView")
            removeRecyclerViewAdapter()
            setNullLiveData()
        }

        Log.e("RecyclerView","Restart RecyclerView $adapterMovie")
        adapterMovie = newRecyclerViewAdapter()

        liveData().observe(lifeCycleOwner) {
            adapterMovie?.submitList(it)
        }

        return adapterMovie!!
    }

}
