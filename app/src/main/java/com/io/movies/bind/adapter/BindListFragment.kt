package com.io.movies.bind.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.io.movies.util.Config

@BindingAdapter("app:isRefreshing")
fun isRefreshing(swipeRefreshLayout: SwipeRefreshLayout, isRefreshing: Boolean){
    swipeRefreshLayout.post {
        swipeRefreshLayout.isRefreshing = isRefreshing
    }
}