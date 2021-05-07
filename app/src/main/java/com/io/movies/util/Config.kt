package com.io.movies.util

import androidx.lifecycle.LiveData
import com.io.movies.model.Movie

object Config {
    const val photo = "https://image.tmdb.org/t/p/w500"
    const val apiKey = "f0c8cfc8c338e88d2157cc91dc4b893c"
}

class MovieLiveDate: LiveData<Movie>() {

    override fun onInactive() {
        super.onInactive()
    }
}