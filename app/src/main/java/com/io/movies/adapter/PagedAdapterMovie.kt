package com.io.movies.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.io.movies.R
import com.io.movies.databinding.ItemMovieInfoBinding
import com.io.movies.model.Movie
import com.io.movies.util.Config

class PagedAdapterMovie constructor(private val update: (Movie) -> Unit): PagedListAdapter<Movie, PagedAdapterMovie.MovieViewHolder>(MyDiffUtil()) {

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
                root.setOnClickListener {
                    Log.e("Movie", "$position  - ${movie.id} :${movie.title}")
                }

                favorite.setOnClickListener {
                    Log.e("Movie","Like")
                    val like = !movie.like
                    movie.like = like
                    favorite.isSelected = like
                    update(movie)
                }
            }
        } ?: Log.e("Paging","null $position")
    }

    class MovieViewHolder(val binding: ItemMovieInfoBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(){
            binding.apply {
                Glide.with(root)
                    .load("${Config.photo}${movie!!.poster}")
                    .error(R.drawable.no_picture)
                    .into(icon)

                favorite.isSelected = movie!!.like
            }
        }
    }
}

class MyDiffUtil: DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.key == newItem.key
    }
}