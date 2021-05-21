package com.io.movies.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.io.movies.R
import com.io.movies.app.App
import com.io.movies.comand.FavoriteCommand
import com.io.movies.comand.MenuListFragmentCommand
import com.io.movies.comand.RecyclerViewListFragmentCommand
import com.io.movies.databinding.FragmentListMoviesBinding
import com.io.movies.model.Movie
import com.io.movies.ui.activity.IDialog
import com.io.movies.util.Config
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class ListMoviesFragment : Fragment() {

    private lateinit var binding: FragmentListMoviesBinding
    private var movieInfo: IDialog? = null

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
        val showMovie: (Movie) -> Unit = {
           // movieInfo?.openAboutOfMovie(id = it.id, isFavorite = it.isFavorite)
        }
        val updateMovie: (Movie) -> Unit = {
            if (viewModel.isFavoriteMode) viewModel.updateMovie(it)
            else favoriteCommand.changeFavoriteStateMovie(movie = it)
        }

        RecyclerViewListFragmentCommand(update = updateMovie,showMovie = showMovie, { viewModel.getLifeData() }, { viewModel.setNullLiveData() })
    }

    private var isConnect: Boolean = true

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        movieInfo = context as IDialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
        Log.e("ListMovies","OnCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.e("ListMovies","OnCreateView")
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

        liveDateСonnect()
        liveDateQuery()
        liveDateFavorite()

        initSwipeRefresh()
    }

    override fun onStart() {
        super.onStart()
        Log.e("ListMovies","onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.e("ListMovies","onStop")
        updateFavoriteList()
    }

    override fun onDestroyView() {
        binding.unbind()
        menuCommand.clear()
        super.onDestroyView()
    }

    override fun onDetach() {
        movieInfo = null
        super.onDetach()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_list, menu)

        menuCommand.searchViewListener(menu.findItem(R.id.action_search).actionView as SearchView, viewModel.query.value!!){
            updateFavoriteList()
            viewModel.postQuery(it)
            newRecycler(isRestart = true)
        }

        menuCommand.favoriteButtonInit(
            favoriteButton = menu.findItem(R.id.action_favorite),
            favoriteButtonSelected = menu.findItem(R.id.action_favorite_selected),
            isFavorite = viewModel.isFavoriteMode.value!!
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        menuCommand.onClickFavoriteButton(item.itemId){
            updateFavoriteList()
            viewModel.postFavorite(it)
            newRecycler(isRestart = true)
        }
        return true
    }

    fun initSwipeRefresh() {
        binding.swipeRefresh.apply {
            setColorSchemeResources(R.color.purple_200)
            setProgressBackgroundColorSchemeResource(R.color.grey)

            setOnRefreshListener {
                viewModel.isRefreshing.set(true)

                Completable.complete()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        Log.e("TAG", "swipe")
                        updateFavoriteList()
                        if (isConnect) viewModel.refresh()
                    }
            }
        }
    }

    private fun liveDateСonnect(){
        Config.isOnline(requireContext()).observe(viewLifecycleOwner){
            isConnect = it

            if (it){
                viewModel.deleteBase()
            } else {
                Snackbar.make(binding.root,"No network",Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun liveDateFavorite(){
        viewModel.isFavoriteMode.observe(viewLifecycleOwner){

        }
    }

    private fun liveDateQuery(){
        viewModel.query.observe(viewLifecycleOwner){

        }
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

}
