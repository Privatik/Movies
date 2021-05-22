package com.io.movies.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.io.movies.R

class MainActivity : AppCompatActivity(), IBackFromAboutMovie {

    private var isClickableBackButtonInMovieFragment = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onSupportNavigateUp(): Boolean {
        if (isClickableBackButtonInMovieFragment) onBackPressed()
        return true
    }

    override fun backButtonClickable(isClickable: Boolean) {
        isClickableBackButtonInMovieFragment = isClickable
    }

}

interface IBackFromAboutMovie{
    fun backButtonClickable(isClickable: Boolean)
}