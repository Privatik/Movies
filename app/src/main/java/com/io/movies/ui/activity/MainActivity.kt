package com.io.movies.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.ViewModelProvider
import com.io.movies.R
import com.io.movies.controller.LoadDialogController
import com.io.movies.databinding.ActivityMainBinding
import com.io.movies.ui.fragment.MovieFragment

class MainActivity : AppCompatActivity(), IDialog, IBackFromAboutMovie {

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
        dialogController.openDialogLoadAboutMovie(id = id, isFavorite = isFavorite)
    }


    override fun closeDialogLoadAboutMovie(bundle: Bundle){
        dialogController.closeDialogLoadAboutMovie()

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
                args = bundle
            )
            addToBackStack(null)
        }
    }

    override fun backButtonClickable(isClickable: Boolean) {
        isClickableBackButtonInMovieFragment = isClickable
    }

}

interface IDialog{
    fun openAboutOfMovie(id: Int, isFavorite: Boolean)
    fun closeDialogLoadAboutMovie(bundle: Bundle)
}

interface IBackFromAboutMovie{
    fun backButtonClickable(isClickable: Boolean)
}