package com.io.movies.comand

import android.graphics.pdf.PdfDocument
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.paging.PagedList
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

    private val showMovie: (Movie) -> Unit = {
        if (!isLoad.get()) {
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
    }

    fun invalidate(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>?){
        (adapter as?  PagedAdapterMovie)?.currentList?.dataSource?.invalidate()
    }

    fun adapter(): PagedAdapterMovie = PagedAdapterMovie(update = update, showMovie = showMovie).also {
            it.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }
}
