package com.io.movies.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.io.movies.R
import com.io.movies.adapter.PagedAdapterMovie
import com.io.movies.app.App
import com.io.movies.databinding.FragmentListBinding
import com.io.movies.model.Movie
import com.io.movies.ui.activity.IUpdateRecycler
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding
    private lateinit var updateSearch: IUpdateRecycler

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private var mAdapter: PagedAdapterMovie? = null

    private val updateRecycler: (String) -> Unit = {
        newRecycler(query = it)
    }

    private val favoriteMode: (String, Boolean) -> Unit = { query, isFavoriteMode ->
        binding.viewmodel?.isFavoriteMode = isFavoriteMode
        newRecycler(query = query)
    }

    private val updateMovie: (Movie) -> Unit = {
        Log.e("TAG","movie ${it.title} -> like - ${it.like}")
        binding.viewmodel!!.updateMovie(movie = it)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        updateSearch = context as IUpdateRecycler
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)

        binding.viewmodel = ViewModelProvider(this, factory).get(ListViewModel::class.java)

        newRecycler()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateSearch.updateRecycler(updateRecycler)
        updateSearch.isFavoriteMode(favoriteMode)

        binding.swipeRefresh.setColorSchemeResources(R.color.purple_200)
        binding.swipeRefresh.setProgressBackgroundColorSchemeResource(R.color.grey)

        binding.swipeRefresh.setOnRefreshListener {
            Completable.complete()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        Log.e("TAG", "swipe")
                        binding.viewmodel?.refresh()
                        binding.swipeRefresh.isRefreshing = false
                    }
        }
    }

    private fun newRecycler(query: String = ""){
        if (mAdapter != null) removeRecycler()
        mAdapter = PagedAdapterMovie(updateMovie)

        binding.progressBar.visibility = View.VISIBLE
        var isLoad = true
        binding.viewmodel?.newRecycler(query = query)

        Log.e("Recycler","create")
        binding.viewmodel?.newLists!!.observe(viewLifecycleOwner){
            Log.e("List","add new")
            mAdapter?.submitList(it)
            if (isLoad) {
                binding.progressBar.visibility = View.GONE
                isLoad = false
            }
        }

        binding.moviesRecycler.adapter = mAdapter
    }


    private fun removeRecycler() {
        Log.e("Recycler","Remove")
        binding.viewmodel?.removeRecycler(lifecycle = viewLifecycleOwner)
        mAdapter?.currentList?.dataSource?.invalidate()
        mAdapter = null
    }
}
