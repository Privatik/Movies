package com.io.movies.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.io.movies.R
import com.io.movies.app.App
import com.io.movies.databinding.FragmentListMoviesBinding
import com.io.movies.model.Movie
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

open class BaseListMoviesFragment : Fragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    lateinit var binding: FragmentListMoviesBinding

    val viewModel: ListMoviesViewModel by lazy {
        ViewModelProvider(this, factory).get(ListMoviesViewModel::class.java)
    }


    val showMovie: (Movie) -> Unit = {
        if (!viewModel.isLoadMovieFragment.get()) {
            viewModel.isLoadMovieFragment.set(true)

            parentFragmentManager.commit {
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

    private var favoriteButton: MenuItem? = null
    private var favoriteButtonSelected: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.e("ListMovies", "OnCreateView")
        binding = FragmentListMoviesBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            supportActionBar?.setDisplayShowHomeEnabled(false)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_list, menu)

        (menu.findItem(R.id.action_search).actionView as SearchView).let { searchView ->
            searchView.maxWidth = binding.toolbar.width - 90
            val queryText = viewModel.query.value!!

            if (queryText.isNotEmpty()) {
                searchView.setQuery(queryText, false)
                searchView.isIconified = false
            }
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                private var subscription: Disposable? = null

                override fun onQueryTextSubmit(query: String): Boolean = false

                override fun onQueryTextChange(newText: String): Boolean {
                    subscription?.dispose()

                    subscription = Observable.timer(800, TimeUnit.MILLISECONDS)
                        .subscribe {
                            viewModel.postQuery(newText)

                            scrollToStart(binding.moviesRecyclerFavorite)
                            scrollToStart(binding.moviesRecycler)
                        }

                    return true
                }
            })
        }


        if (this.favoriteButton == null) {
            favoriteButton = menu.findItem(R.id.action_favorite)
            favoriteButtonSelected = menu.findItem(R.id.action_favorite_selected)

            if (viewModel.getIsFavorite()) {
                favoriteButton!!.isVisible = false
            } else {
                favoriteButtonSelected!!.isVisible = false
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            favoriteButton?.itemId -> {
                Log.e("Menu", "Selected")
                favoriteButtonSelected!!.isVisible = true
                favoriteButton!!.isVisible = false
                viewModel.postFavorite(true)
                Log.e("isFavoriteMenu", "is null-? $favoriteButtonSelected")
            }
            favoriteButtonSelected?.itemId -> {
                Log.e("Menu", "Not Selected")
                favoriteButtonSelected!!.isVisible = false
                favoriteButton!!.isVisible = true
                viewModel.postFavorite(false)
            }
            else -> {
                Log.e("Menu", "Empty")
            }
        }
        return true
    }

    override fun onDestroyView() {
        favoriteButton = null
        favoriteButtonSelected = null
        super.onDestroyView()
    }

    fun scrollToStart(recyclerView: RecyclerView) {
        recyclerView.let {
            it.postDelayed({
                it.scrollToPosition(0)
            }, 400)
        }
    }
}