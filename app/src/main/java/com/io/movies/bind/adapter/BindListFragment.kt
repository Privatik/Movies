package com.io.movies.bind.adapter

import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

@BindingAdapter("app:isRefreshing")
fun isRefreshing(swipeRefreshLayout: SwipeRefreshLayout, isRefreshing: Boolean){
    swipeRefreshLayout.post {
        swipeRefreshLayout.isRefreshing = isRefreshing
    }
}