package com.io.movies.comand

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.RecyclerView
import com.io.movies.R
import com.io.movies.adapter.PagedAdapterMovie
import com.io.movies.model.Movie
import com.io.movies.ui.fragment.MovieFragment

class RecyclerViewListFragmentCommand(
    private val update: (Movie) -> Unit,
    private val fragmentManager: FragmentManager,
    private val isLoad: ObservableBoolean,
) {

    var adapterMovie: PagedAdapterMovie? = null

    private val showMovie: (Movie) -> Unit = {
        isLoad.set(true)

        fragmentManager.commit {
            setReorderingAllowed(true)
            setCustomAnimations(
                R.anim.slide_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out
            )

            replace<MovieFragment>(
                R.id.fragment_container_view,
                args = MovieFragment.setAndGetBundle(it.id, it.isFavorite)
            )
            addToBackStack(null)
        }
    }

    fun removeRecyclerViewAdapter(){
        adapterMovie = null
    }

    fun invalidate(){
        adapterMovie?.currentList?.dataSource?.invalidate()
    }


    fun newRecyclerViewAdapter(): PagedAdapterMovie{
        if (adapterMovie != null) {
            Log.e("RecyclerView", "set null RecyclerView")
            removeRecyclerViewAdapter()
        }

        return PagedAdapterMovie(update = update, showMovie = showMovie).also {
            adapterMovie = it
            adapterMovie!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }
    }
}
