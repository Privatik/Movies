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
import com.io.movies.adapter.PagedAdapterMovie
import com.io.movies.app.App
import com.io.movies.comand.MenuListFragmentCommand
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

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private var mAdapter: PagedAdapterMovie? = null


    private val showMovie: (Int, Boolean) -> Unit = { id, isFavorite ->
        Log.e("AboutMovie","Open")
        movieInfo.openAboutOfMovie(id = id, isFavorite = isFavorite)
    }

    private val updateMovie: (Movie) -> Unit = {
        Log.e("TAG", "movie ${it.title} -> like - ${it.isFavorite}")
        viewModel.updateMovie(movie = it)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        movieInfo = context as IMovie
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListMoviesBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel

        checkNetwork()

        newRecycler()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            setHasOptionsMenu(true)
        }

        binding.swipeRefresh.setColorSchemeResources(R.color.purple_200)
        binding.swipeRefresh.setProgressBackgroundColorSchemeResource(R.color.grey)

        binding.swipeRefresh.setOnRefreshListener {
            Completable.complete()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Log.e("TAG", "swipe")
                    checkNetwork()
                    viewModel.refresh()
                    viewModel.isRefreshing.set(false)
                }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_list, menu)

        menuCommand.searchViewListener(menu.findItem(R.id.action_search).actionView as SearchView){
            viewModel.query = it
            newRecycler()
        }

        menuCommand.favoriteButtonInit(itemNotSelected = menu.findItem(R.id.action_favorite), itemSelected = menu.findItem(R.id.action_favorite_selected))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        menuCommand.onClickFavoriteButton(item.itemId){
            viewModel.isFavoriteMode = it
            newRecycler()
        }
        return true
    }

    private fun newRecycler() {
        if (mAdapter != null) removeRecycler()
        mAdapter = PagedAdapterMovie(updateMovie, showMovie)

        viewModel.newRecycler(query = viewModel.query)

        Log.e("Recycler", "create")
       viewModel.newLists!!.observe(viewLifecycleOwner) {
            Log.e("List", "add new")
            mAdapter?.submitList(it)
        }

        binding.moviesRecycler.adapter = mAdapter
    }


    private fun removeRecycler() {
        Log.e("Recycler", "Remove")
        viewModel.newLists!!.removeObservers(viewLifecycleOwner)
        mAdapter?.currentList?.dataSource?.invalidate()
        mAdapter = null
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
