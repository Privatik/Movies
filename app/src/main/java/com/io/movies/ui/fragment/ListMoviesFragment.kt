package com.io.movies.ui.fragment

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
        MenuListFragmentCommand()
    }

    private val recyclerViewCommand by lazy {
        val update: (Movie) -> Unit ={ viewModel.updateFavoriteStateMovie(it) }

        RecyclerViewListFragmentCommand(update = update, fragmentManager = parentFragmentManager,isLoad = viewModel.isLoadAboutMovie)
    }

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.e("ListMovies","OnCreateView")
        binding = FragmentListMoviesBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            setHasOptionsMenu(true)
        }
<<<<<<< HEAD
        connectLiveData()

        swipeRefreshLoad()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_list, menu)

        menuCommand.searchViewListener(menu.findItem(R.id.action_search).actionView as SearchView, viewModel.getQuery()){
            viewModel.apply {
                updateMovies()
                postQuery(it)
            }
        }

        menuCommand.favoriteButtonInit(
            favoriteButton = menu.findItem(R.id.action_favorite),
            favoriteButtonSelected = menu.findItem(R.id.action_favorite_selected),
            isFavorite = viewModel.getIsFavorite()
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        menuCommand.onClickFavoriteButton(item.itemId){
            viewModel.apply {
                updateMovies()
                postFavorite(it)
            }
        }
        return true
    }

    override fun onStop() {
        super.onStop()
        viewModel.updateMovies()
    }

    override fun onDestroyView() {
        viewModel.isLoadAboutMovie.set(false)
        binding.unbind()
        menuCommand.clear()
        recyclerViewCommand.removeRecyclerViewAdapter()
        super.onDestroyView()
    }

    private fun swipeRefreshLoad() {
=======
<<<<<<< Updated upstream

=======
        connectLiveData()

        swipeRefreshLoad()
    }

    private fun swipeRefreshLoad() {
>>>>>>> Stashed changes
>>>>>>> main
        binding.swipeRefresh.apply {
            setColorSchemeResources(R.color.purple_200)
            setProgressBackgroundColorSchemeResource(R.color.grey)

            setOnRefreshListener {
                Completable.complete()
                    .subscribe {
                        Log.e("TAG", "swipe")
                        viewModel.updateMovies()
                        if (!viewModel.getIsFavorite()) {
                            if (Config.isConnect == true) viewModel.refresh()
                        } else {
                            isRefreshing = false
                        }
                    }
            }
        }
    }

<<<<<<< HEAD
    private fun connectLiveData(){
        val isNotConnect: () -> Unit = {
            Snackbar.make(binding.root, "No network", Snackbar.LENGTH_SHORT).show()
        }

        val updateLoad: () -> Unit = {
            recyclerViewCommand.invalidate()
=======
<<<<<<< Updated upstream
    override fun onStop() {
        super.onStop()
        updateFavoriteList()
    }
=======
    private fun connectLiveData(){
        val isNotConnect: () -> Unit = {
              Snackbar.make(binding.root, "No network", Snackbar.LENGTH_SHORT).show()
        }

        val updateLoad: () -> Unit = {
            recyclerViewCommand.invalidate()
        }
>>>>>>> Stashed changes

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_list, menu)

<<<<<<< Updated upstream
        menuCommand.searchViewListener(menu.findItem(R.id.action_search).actionView as SearchView, viewModel.query){
            updateFavoriteList()
            viewModel.query = it
            newRecycler(isRestart = true)
=======
        if(Config.isConnect != null){
            firstStart()
>>>>>>> Stashed changes
>>>>>>> main
        }

        val firstStart: () -> Unit = {
            recyclerViewLiveData()
        }

        if(Config.isConnect == true){
            firstStart()
        }

<<<<<<< HEAD
        viewModel.isConnect(
            liveDataConnect = Config.isOnline(requireContext().applicationContext)!!,
            isNotConnect = isNotConnect,
            updateLoad = updateLoad,
            firstStart = firstStart)
    }


    private fun recyclerViewLiveData(){
        viewModel.mediatorUpdateRecyclerView.observe(viewLifecycleOwner){
            if (it == null) return@observe
            newRecycler()
=======
<<<<<<< Updated upstream
    private fun newRecycler(isRestart: Boolean) {
        binding.moviesRecycler.adapter = recyclerViewCommand.newRecycler(isRestart = isRestart)
    }

    private fun updateFavoriteList(){
        favoriteCommand.updateListFavorite().let {
            viewModel.updateMovies(it)
            it.clear()
=======
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_list, menu)

        menuCommand.searchViewListener(menu.findItem(R.id.action_search).actionView as SearchView, viewModel.getQuery()){
            viewModel.apply {
                updateMovies()
                postQuery(it)
            }
        }

        menuCommand.favoriteButtonInit(
            favoriteButton = menu.findItem(R.id.action_favorite),
            favoriteButtonSelected = menu.findItem(R.id.action_favorite_selected),
            isFavorite = viewModel.getIsFavorite()
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        menuCommand.onClickFavoriteButton(item.itemId){
            viewModel.apply {
                updateMovies()
                postFavorite(it)
            }
        }
        return true
    }

    override fun onStop() {
        super.onStop()
        viewModel.updateMovies()
        viewModel.isLoadAboutMovie.apply { if (get()) set(false) }
    }

    override fun onDestroyView() {
        binding.unbind()
        menuCommand.clear()
        viewModel.clear()
        recyclerViewCommand.removeRecyclerViewAdapter()
        super.onDestroyView()
    }

    private fun newRecycler() {
        viewModel.apply {
            if (newLists?.hasObservers() == true) {
                Log.e("UpdateRecycler","newList set null")
                newLists!!.removeObservers(viewLifecycleOwner)
                newLists = null
            }
>>>>>>> Stashed changes
>>>>>>> main
        }
    }

    private fun newRecycler() {
        viewModel.apply {
            if (newLists?.hasObservers() == true) {
                Log.e("UpdateRecycler","newList set null")
                newLists!!.removeObservers(viewLifecycleOwner)
                newLists = null
            }
        }

        viewModel.updateQuery()
        Log.e("RecyclerView","new create Recycler")
        binding.moviesRecycler.adapter = recyclerViewCommand.newRecyclerViewAdapter().also {  adapter ->
            viewModel.newLists?.observe(viewLifecycleOwner){
                adapter.submitList(it)
            }
        }
    }
}
