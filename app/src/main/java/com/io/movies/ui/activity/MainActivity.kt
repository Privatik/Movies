package com.io.movies.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
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

class MainActivity : AppCompatActivity(), IMovie, IBackFromAboutMovie {

    private lateinit var binding: ActivityMainBinding

    private val dialogController by lazy { LoadDialogController(supportFragmentManager) }

    private var isClickableBackButtonInMovieFragment = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_main)

        binding.viewmodel = ViewModelProvider(this).get(MainViewHModel::class.java)


    }

    override fun onSupportNavigateUp(): Boolean {
        if (isClickableBackButtonInMovieFragment) onBackPressed()
        return true
    }

    override fun openAboutOfMovie(id: Int, isFavorite: Boolean) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            setCustomAnimations(
                R.anim.slide_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out
            )

            replace<MovieFragment>(
                R.id.fragment_container_view,
                args = MovieFragment.newInstanceBundle(id = id, isFavorite = isFavorite)
            )
            addToBackStack(null)
        }

        dialogController.openDialogLoadAboutMovie()
    }


    override fun closeDialogLoadAboutMovie()
     = dialogController.closeDialogLoadAboutMovie()

    override fun backButtonClickable(isClickable: Boolean) {
        isClickableBackButtonInMovieFragment = isClickable
    }

}

interface IMovie{
    fun openAboutOfMovie(id: Int, isFavorite: Boolean)
    fun closeDialogLoadAboutMovie()
}

interface IBackFromAboutMovie{
    fun backButtonClickable(isClickable: Boolean)
}