package com.io.movies.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.io.movies.R
import com.io.movies.adapter.PagedAdapterMovie
import com.io.movies.util.Config
import io.reactivex.Completable


class ListMoviesFragment : BaseListMoviesFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        connectLiveData()
        if (!viewModel.isFirstStart){
            initMovieInfoAndFavoriteAdapters()
        }

        binding.swipeRefresh.apply {
            setColorSchemeResources(R.color.purple_200)
            setProgressBackgroundColorSchemeResource(R.color.grey)

            setOnRefreshListener {
                Completable.complete()
                    .subscribe {
                        Log.e("TAG", "swipe")
                        viewModel.updateListFavoriteMovie()
                        if (Config.isConnect == true) viewModel.refresh()
                        else isRefreshing = false
                    }
            }
        }
    }

    private fun connectLiveData() {
        Config.isOnline(requireContext().applicationContext)?.observe(viewLifecycleOwner){
            if (viewModel.isFirstStart){
                viewModel.load()
                if (it) {
                    viewModel.deleteBase()
                    initMovieInfoAndFavoriteAdapters()
                } else{
                    viewModel.setCount()
                    Config.snackBarNoNetwork(binding.root)
                }
                viewModel.isFirstStart = false
            }else{
                if (it) {
                    viewModel.invalidate()
                } else{
                    viewModel.newConnectNetwork()
                    Config.snackBarNoNetwork(binding.root)
                }
            }
            Log.e("Tag","new connect - $it")
        }
    }

    private fun initMovieInfoAndFavoriteAdapters(){
        binding.moviesRecycler.adapter = PagedAdapterMovie(update = {viewModel.updateFavoriteStateMovie(it)}, showMovie = showMovie).also{ adapter ->
            adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            viewModel.livedataMovieInfo.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
        }

        binding.moviesRecyclerFavorite.adapter = PagedAdapterMovie(update = {viewModel.updateFavoriteStateMovie(it)}, showMovie = showMovie).also{ adapter ->
            adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            viewModel.livedataFavorite.observe(viewLifecycleOwner) {
                Log.e("FavoriteList", "add ${it.size}")
                adapter.submitList(it)
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
        viewModel.isRefreshing.set(false)
      //  viewModel.clear()
        super.onDestroyView()
    }

}
