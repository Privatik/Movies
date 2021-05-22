package com.io.movies.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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
        val loadAboutMovie: (Movie) -> Unit ={ viewModel.load(it)}

        RecyclerViewListFragmentCommand(update = update, loadAboutMovie = loadAboutMovie,isLoad = viewModel.isLoadAboutMovie)
    }

    private var isConnect: Boolean = true
    var isStartApp = true

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
        viewModel.isLoadAboutMovie.set(false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            setHasOptionsMenu(true)
        }

        connectLiveData()
        loadAboutMovieLiveData()

        swipeRefreshLoad()
    }


    override fun onStop() {
        super.onStop()
        viewModel.updateMovies()
    }

    override fun onDestroyView() {
        viewModel.scrollY = (binding.moviesRecycler.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
        Log.e("Scroll","${viewModel.scrollY}")
        viewModel.setNullParamsLiveDatas()
        binding.unbind()
        menuCommand.clear()
        recyclerViewCommand.removeRecyclerViewAdapter()
        super.onDestroyView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_list, menu)

        menuCommand.searchViewListener(menu.findItem(R.id.action_search).actionView as SearchView, viewModel.query.value!!){
            viewModel.apply {
                updateMovies()
                postQuery(it)
            }
        }

        menuCommand.favoriteButtonInit(
            favoriteButton = menu.findItem(R.id.action_favorite),
            favoriteButtonSelected = menu.findItem(R.id.action_favorite_selected),
            isFavorite = viewModel.isFavoriteMode.value
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

    private fun swipeRefreshLoad() {
        binding.swipeRefresh.apply {
            setColorSchemeResources(R.color.purple_200)
            setProgressBackgroundColorSchemeResource(R.color.grey)

            setOnRefreshListener {
                Completable.complete()
                    .subscribe {
                        Log.e("TAG", "swipe")
                        viewModel.updateMovies()
                        if (viewModel.isFavoriteMode.value == null || (!viewModel.isFavoriteMode.value!!)) {
                            if (isConnect) viewModel.refresh()
                        } else {
                            isRefreshing = false
                        }
                    }
            }
        }
    }

    private fun connectLiveData(){
        var isRestart = true

        Config.isOnline(requireContext()).observe(viewLifecycleOwner){
            if (isStartApp){
                if (it) viewModel.deleteBase()

                isStartApp = false
            }
            if (isRestart){
                viewModel.apply {
                    initLiveData(liveData = query)
                    postFavorite(null)
                    initLiveData(liveData = isFavoriteMode)
                }

                isRestart = false
            }

            Log.e("Connect","new connect $it")
            isConnect = it
            if (!it) Snackbar.make(binding.root,"No network",Snackbar.LENGTH_SHORT).show()
        }
    }


    private fun initLiveData(liveData: LiveData<*>){
        liveData.observe(viewLifecycleOwner){
            if (it == null) return@observe
            newRecycler(updateLiveData = {viewModel.updateQuery()})
        }
    }

    private fun newRecycler(updateLiveData: () -> Unit) {
        if ( binding.moviesRecycler.adapter != null) {
            viewModel.apply {
                if (newLists != null) {
                    newLists!!.removeObservers(viewLifecycleOwner)
                    newLists = null
                }
            }
        }

        updateLiveData()
        Log.e("RecyclerView","new create ${viewModel.newLists}")
        binding.moviesRecycler.adapter = recyclerViewCommand.newRecyclerViewAdapter().also {  adapter ->
            viewModel.newLists?.observe(viewLifecycleOwner){
                adapter.submitList(it)
            }
        }
    }

    private fun loadAboutMovieLiveData(){
        viewModel.loadAboutMovie.observe(viewLifecycleOwner){
            if (it == null) return@observe
            Log.e("Tag","open aboutMovie")
            activity?.supportFragmentManager?.commit {
                setReorderingAllowed(true)
                setCustomAnimations(
                    R.anim.slide_in,
                    R.anim.fade_out,
                    R.anim.fade_in,
                    R.anim.slide_out
                )

                replace<MovieFragment>(
                    R.id.fragment_container_view,
                    args = MovieFragment.setAndGetBundle(it.first, it.second)
                )
                addToBackStack(null)
            }
        }
    }
}
