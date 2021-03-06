package com.io.movies.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.io.movies.R
import com.io.movies.databinding.ItemMovieInfoBinding
import com.io.movies.model.Movie

class PagedAdapterMovie constructor(private val update: (Movie) -> Unit, private val showMovie: (Movie) -> Unit): PagedListAdapter<Movie, PagedAdapterMovie.MovieViewHolder>(MyDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder = MovieViewHolder(
            ItemMovieInfoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
            )
    )

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        getItem(position)?.let{ movie ->
            holder.binding.apply {
                this.movie = movie
                holder.bind()
                root.apply {
                    animation = AnimationUtils.loadAnimation(context, R.anim.slide_item_movie)
                    setOnClickListener {
                        showMovie(movie)
                    }
                }

                favorite.setOnClickListener {
                    Log.e("Movie","Like")
                    val like = !movie.isFavorite
                    movie.isFavorite = like
                    favorite.isSelected = like
                    update(movie)
                }
            }
        } ?: Log.e("Paging","null $position")
    }

    class MovieViewHolder(val binding: ItemMovieInfoBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(){
            binding.apply {
                favorite.isSelected = movie!!.isFavorite
            }
        }
    }
}

class MyDiffUtil: DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.isFavorite == newItem.isFavorite
    }
}