package com.io.movies.comand

import com.io.movies.model.Movie

class FavoriteCommand {

    private val list = mutableListOf<Movie>()

    fun changeFavoriteStateMovie(movie: Movie){
        movie.let {
            if (movie !in list){
                list.add(it)
            } else{
                list.remove(it)
            }
        }
    }

    fun updateListFavorite(): MutableList<Movie> = list
}