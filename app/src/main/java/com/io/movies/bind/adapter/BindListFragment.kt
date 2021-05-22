package com.io.movies.bind.adapter

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.io.movies.R
import com.io.movies.util.Config

@BindingAdapter("app:isRefreshing")
fun isRefreshing(swipeRefreshLayout: SwipeRefreshLayout, isRefreshing: Boolean){
    swipeRefreshLayout.post {
        swipeRefreshLayout.isRefreshing = isRefreshing
    }
}

@BindingAdapter("app:loadState")
fun loadState(frameLayout: FrameLayout, isVisibility: Boolean) {
    frameLayout.visibility = if (isVisibility) View.VISIBLE else View.GONE
}

@BindingAdapter("app:notEnabledForLoad")
fun notEnabledForLoad(view: View, isLoad: Boolean) {
    view.isClickable = !isLoad
    view.isEnabled = !isLoad
}

@BindingAdapter("app:scrollTo")
fun scrollTo(recyclerView: RecyclerView, y: Int) {
    recyclerView.layoutManager?.scrollToPosition(y)
}
