package com.io.movies.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.io.movies.databinding.ItemMovieInfoBinding
import com.io.movies.model.Movie
import com.io.movies.util.Config

class PagedAdapterMovie: PagedListAdapter<Movie, PagedAdapterMovie.MovieViewHolder>(MyDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder = MovieViewHolder(
            ItemMovieInfoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
            )
    )

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        getItem(position)?.let{
            holder.binding.movie = it
            holder.bind()
        } ?: Log.e("Paging","null $position")
    }

    class MovieViewHolder(val binding: ItemMovieInfoBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(){
            Glide.with(binding.root)
                    .load("${Config.photo}${binding.movie!!.poster}")
                    .into(binding.icon)
        }
    }
}

class MyDiffUtil: DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}