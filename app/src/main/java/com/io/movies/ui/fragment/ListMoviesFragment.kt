package com.io.movies.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.io.movies.R
import com.io.movies.app.App
import com.io.movies.comand.MenuListFragmentCommand
import com.io.movies.comand.RecyclerViewListFragmentCommand
import com.io.movies.databinding.FragmentListMoviesBinding
import com.io.movies.model.Movie
import com.io.movies.util.Config
import io.reactivex.Completable
import javax.inject.Inject


class ListMoviesFragment : Fragment() {

    private lateinit var binding: FragmentListMoviesBinding

    private val viewModel: ListMoviesViewModel by lazy {
        ViewModelProvider(this, factory).get(ListMoviesViewModel::class.java)
    }

    private val menuCommand by lazy {
        MenuListFragmentCommand(viewModel.isFavoriteModeMutable, viewModel.queryMutable)
    }

    private val recyclerViewCommand by lazy {
        val update: (Movie) -> Unit = { viewModel.updateFavoriteStateMovie(it) }

        RecyclerViewListFragmentCommand(
            update = update,
            fragmentManager = parentFragmentManager,
            isLoad = viewModel.isLoadMovieFragment
        )
    }

    @Inject
    lateinit var factory: ViewModelProvider.Factory

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
        }

        connectLiveData()

         binding.moviesRecycler.adapter = recyclerViewCommand.adapter().also { adapter->
            viewModel.livedataMovieInfo.observe(viewLifecycleOwner){
                adapter.submitList(it)
            }
        }

        binding.moviesRecyclerFavorite.adapter = recyclerViewCommand.adapter().also { adapter ->
            viewModel.livedataFavorite.observe(viewLifecycleOwner){
                Log.e("FavoriteList","add ${it.size}")
                adapter.submitList(it)
            }
        }

        binding.swipeRefresh.apply {
            setColorSchemeResources(R.color.purple_200)
            setProgressBackgroundColorSchemeResource(R.color.grey)

            setOnRefreshListener {
                Completable.complete()
                    .subscribe {
                        Log.e("TAG", "swipe")
                        viewModel.updateListFavoriteMovie()
                        if (!viewModel.getIsFavorite()) {
                            if (Config.isConnect == true) viewModel.refresh()
                        } else {
                            isRefreshing = false
                        }
                    }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        Log.e("Tag","inti menu")
        inflater.inflate(R.menu.menu_list, menu)

        menuCommand.searchViewListener(menu.findItem(R.id.action_search).actionView as SearchView)

        menuCommand.favoriteButtonInit(
            favoriteButton = menu.findItem(R.id.action_favorite),
            favoriteButtonSelected = menu.findItem(R.id.action_favorite_selected)
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        menuCommand.onClickFavoriteButton(item.itemId)
        return true
    }

    private fun connectLiveData() {
        Config.isOnline(requireContext().applicationContext)?.observe(viewLifecycleOwner){
            if (viewModel.isFirstStart){
                viewModel.load()
                if (it) viewModel.deleteBase() else Config.snackBarNoNetwork(binding.root)
                viewModel.isFirstStart = false
            }else{
                if (it) {
                    viewModel.newConnectNetwork()
                    recyclerViewCommand.invalidate(binding.moviesRecycler.adapter)
                    recyclerViewCommand.invalidate(binding.moviesRecyclerFavorite.adapter)
                } else{
                    Config.snackBarNoNetwork(binding.root)
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.updateListFavoriteMovie()
        viewModel.isLoadMovieFragment.apply { if (get()) set(false) }
        Log.e("ListFragment", "onStop")
    }

    override fun onDestroyView() {
        Log.e("ListFragment", "onDestroyView")
        binding.unbind()
        viewModel.clear()
        menuCommand.clear()
        super.onDestroyView()
    }

}
