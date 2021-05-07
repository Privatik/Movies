package com.io.movies.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.io.movies.R
import com.io.movies.adapter.PagedAdapterMovie
import com.io.movies.app.App
import com.io.movies.databinding.FragmentListBinding
import com.io.movies.decoration.MovieItemDecoration
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private var mAdapter: PagedAdapterMovie? = null

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

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.moviesRecycler.addItemDecoration(
                MovieItemDecoration(
                ResourcesCompat.getDrawable(resources, R.drawable.movie_item_decoration, null)!!
        ))

        binding.swipeRefresh.setColorSchemeResources(R.color.purple_200)
        binding.swipeRefresh.setProgressBackgroundColorSchemeResource(R.color.grey)

        binding.swipeRefresh.setOnRefreshListener {
            Observable.timer(500, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        Log.e("TAG", "swipe")
                        mAdapter?.shuffle()
                        binding.swipeRefresh.isRefreshing = false
                    }
        }

        newRecycler()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_list, menu)

        val search = menu.findItem(R.id.action_search).actionView as SearchView
        search.setPadding(5,0,5,0)
        search.queryHint = resources.getString(R.string.edit_movie)

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            private var subscription: Disposable? = null

            override fun onQueryTextSubmit(query: String): Boolean {
                Log.e("TAG", "submit $query")
                if (query.isEmpty()){
                    newRecycler()
                }
                else {
                    newRecycler(query = query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (subscription != null) {
                    subscription!!.dispose()
                }

                subscription = Observable.timer(800, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            onQueryTextSubmit(newText)
                        }
                return true
            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.action_favorite -> {
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun newRecycler(query: String = ""){
        if (mAdapter != null) removeRecycler()

        mAdapter = PagedAdapterMovie()

        binding.viewmodel?.newRecycler(query = query)
        binding.viewmodel?.newLists!!.observe(viewLifecycleOwner){
            Log.e("Recycler", "new list $it")
            mAdapter?.submitList(it)
        }

        binding.moviesRecycler.adapter = mAdapter
        mAdapter!!.notifyDataSetChanged()
    }


    private fun removeRecycler() {
        Log.e("Recycler","Remove")
        binding.viewmodel?.removeRecycler(lifecycle = viewLifecycleOwner)
        mAdapter?.currentList?.dataSource?.invalidate()
        mAdapter = null
    }
}