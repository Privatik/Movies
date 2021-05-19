package com.io.movies.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.io.movies.R
import com.io.movies.app.App
import com.io.movies.comand.FavoriteCommand
import com.io.movies.comand.MenuListFragmentCommand
import com.io.movies.comand.RecyclerViewListFragmentCommand
import com.io.movies.databinding.FragmentListMoviesBinding
import com.io.movies.model.Movie
import com.io.movies.ui.activity.IMovie
import com.io.movies.util.Config
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class ListMoviesFragment : Fragment() {

    private lateinit var binding: FragmentListMoviesBinding
    private lateinit var movieInfo: IMovie

    private val viewModel: ListMoviesViewModel by lazy {
        ViewModelProvider(this, factory).get(ListMoviesViewModel::class.java)
    }

    private val menuCommand by lazy {
        MenuListFragmentCommand()
    }

    private val favoriteCommand by lazy{
        FavoriteCommand()
    }

    private val recyclerViewCommand by lazy {
        val showMovie: (Movie) -> Unit = {movieInfo.openAboutOfMovie(id = it.id, isFavorite = it.isFavorite) }
        val updateMovie: (Movie) -> Unit = {
            if (viewModel.isFavoriteMode) viewModel.updateMovie(it)
            else favoriteCommand.changeFavoriteStateMovie(movie = it)
        }

        RecyclerViewListFragmentCommand(update = updateMovie,showMovie = showMovie, { viewModel.getLifeData() }, { viewModel.setNullLiveData() })
    }

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        movieInfo = context as IMovie
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
        checkNetwork()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListMoviesBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel

        recyclerViewCommand.lifeCycleOwner = viewLifecycleOwner
        newRecycler(isRestart = false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            setHasOptionsMenu(true)
        }

        binding.swipeRefresh.apply {
            setColorSchemeResources(R.color.purple_200)
            setProgressBackgroundColorSchemeResource(R.color.grey)

            setOnRefreshListener {
                Completable.complete()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        Log.e("TAG", "swipe")
                        updateFavoriteList()
                        checkNetwork()
                        viewModel.refresh()
                        if (viewModel.isFavoriteMode) isRefreshing = false
                        Log.e("TAG", "swipe isRefresh - ${viewModel.isRefreshing.get()}")
                    }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        updateFavoriteList()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_list, menu)

        menuCommand.searchViewListener(menu.findItem(R.id.action_search).actionView as SearchView, viewModel.query){
            updateFavoriteList()
            viewModel.query = it
            newRecycler(isRestart = true)
        }

        menuCommand.favoriteButtonInit(itemNotSelected = menu.findItem(R.id.action_favorite), itemSelected = menu.findItem(R.id.action_favorite_selected))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        menuCommand.onClickFavoriteButton(item.itemId){
            updateFavoriteList()
            viewModel.isFavoriteMode = it
            newRecycler(isRestart = true)
        }
        return true
    }

    private fun newRecycler(isRestart: Boolean) {
        binding.moviesRecycler.adapter = recyclerViewCommand.newRecycler(isRestart = isRestart)
    }

    private fun updateFavoriteList(){
        favoriteCommand.updateListFavorite().let {
            viewModel.updateMovies(it)
            it.clear()
        }
    }

    private fun checkNetwork() {
        viewModel.isConnect = Config.isOnline(requireContext()).also{
            if (it) {
               viewModel.deleteBase()
            } else {
                Toast.makeText(requireContext(), "Нет сети", Toast.LENGTH_LONG).show()
            }
        }
    }
}
