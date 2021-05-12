package com.io.movies.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.io.movies.R
import com.io.movies.controller.LoadDialogController
import com.io.movies.databinding.ActivityMainBinding
import com.io.movies.ui.fragment.MovieFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), IUpdateRecycler, IMovie {

    private lateinit var pair: Pair<MenuItem, MenuItem>

    private lateinit var updateRecycler: (String) -> Unit
    private lateinit var isFavoriteMode: (String, Boolean) -> Unit

    private lateinit var binding: ActivityMainBinding

    private val dialogController by lazy { LoadDialogController(supportFragmentManager) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_main)

        binding.viewmodel = ViewModelProvider(this).get(MainViewHModel::class.java)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.action_favorite -> {
                Log.e("Menu", "Selected")
                pair.second.isVisible = true
                pair.first.isVisible = false
                binding.viewmodel?.query?.let { isFavoriteMode(it,true) }
                true
            }
            R.id.action_favorite_selected -> {
                Log.e("Menu", "Not Selected")
                pair.second.isVisible = false
                pair.first.isVisible = true
                binding.viewmodel?.query?.let { isFavoriteMode(it,false) }
                true
            }
            else -> {
                Log.e("Menu", "Empty")
                super.onOptionsItemSelected(item)
            }
        }
    }

    @SuppressLint("WrongConstant")
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_list, menu)
        val search = menu.findItem(R.id.action_search).actionView as SearchView

        val second = menu.findItem(R.id.action_favorite_selected)
        second.isVisible = false
        pair = Pair(menu.findItem(R.id.action_favorite),second)

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            private var subscription: Disposable? = null

            override fun onQueryTextSubmit(query: String): Boolean {
                Log.e("TAG", "submit $query")
                binding.viewmodel?.query = query
                updateRecycler(query)
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
        return true
    }

    override fun onBackPressed() {
        supportFragmentManager.apply {
            if (backStackEntryCount > 0) {
                popBackStack()
            } else {
                super.onBackPressed()
            }
        }
    }

    override fun updateRecycler(search: (String) -> Unit) {
        updateRecycler = search
    }

    override fun isFavoriteMode(isFavoriteMode: (String, Boolean) -> Unit) {
        this.isFavoriteMode = isFavoriteMode
    }

    override fun openAboutOfMovie(id: Int) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            addToBackStack(null)
            add<MovieFragment>(R.id.fragment_container_view, args = MovieFragment.newInstanceBundle(id = id))
        }

        dialogController.openDialogLoadAboutMovie()
    }

    override fun closeDialogLoadAboutMovie() = dialogController.closeDialogLoadAboutMovie()

}

interface IUpdateRecycler{
    fun updateRecycler(search: (String) -> Unit)
    fun isFavoriteMode(isFavoriteMode: (String, Boolean) -> Unit)
}

interface IMovie{
    fun openAboutOfMovie(id: Int)
    fun closeDialogLoadAboutMovie()
}