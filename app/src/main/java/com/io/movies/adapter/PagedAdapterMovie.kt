package com.io.movies.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.io.movies.R
import com.io.movies.databinding.ItemMovieInfoBinding
import com.io.movies.model.Movie
import com.io.movies.model.replace
import com.io.movies.util.Config
import kotlin.random.Random

class PagedAdapterMovie: PagedListAdapter<Movie, PagedAdapterMovie.MovieViewHolder>(MyDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder = MovieViewHolder(
            ItemMovieInfoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
            )
    )

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        getItem(position)?.let{ movie ->
            holder.binding.movie = movie
            holder.bind()
            holder.binding.root.setOnClickListener {
                Log.e("Movie", movie.title)
            }
        } ?: Log.e("Paging","null $position")
    }

    fun shuffle(){
        currentList?.let {
            Log.e("Shuffle","${it.size}")
            val arr = (0 until it.size).toMutableList().shuffled()
            for (i in 0 until it.size) {
                if (i != arr[i]){
                    val item1 = getItem(i)!!
                    val item2 = getItem(arr[i])!!
                    Log.e("Shuffle","from  1 - $item1  2 - $item2")
                    item1.replace(item2)

                    Log.e("Shuffle","to 1 - $item1  2 - $item2")
                }
            }
        }
       notifyDataSetChanged()
    }

    class MovieViewHolder(val binding: ItemMovieInfoBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(){
            Glide.with(binding.root)
                    .load("${Config.photo}${binding.movie!!.poster}")
                    .error(R.drawable.no_picture)
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