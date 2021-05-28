package com.io.movies.bind.adapter

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.io.movies.R
import com.io.movies.util.Config

@BindingAdapter("app:loadImageOrNoPoster")
fun loadImageOrNoPoster(view: ImageView, url: String?) {
    Glide.with(view.context)
        .load("${Config.photo}${url}")
        .placeholder(R.drawable.glide_placeholder)
        .error(R.drawable.no_poster)
        .skipMemoryCache(true)
        .into(view)
}

@BindingAdapter("app:loadImageOrNoImage")
fun loadImageOrNoImage(view: ImageView, url: String?) {
    Glide.with(view.context)
        .load("${Config.photo}${url}")
        .placeholder(R.drawable.glide_placeholder)
        .error(R.drawable.no_image)
        .skipMemoryCache(true)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .into(view)
}

@BindingAdapter("app:loadImage")
fun loadImage(view: ImageView, url: String?) {
    Glide.with(view.context)
        .load("${Config.photo}${url}")
        .placeholder(R.drawable.glide_placeholder)
     //   .signature(ObjectKey(url))
        .skipMemoryCache(true)
        .into(view)
}

@BindingAdapter("app:isGoneOrVisibility")
fun isGoneOrVisibility(view: View, isNotVisibility: Boolean) {
    view.visibility = if (isNotVisibility) View.GONE else View.VISIBLE
}

@BindingAdapter("app:isVisibilityOrGone")
fun isVisibilityOrGone(view: View, isVisibility: Boolean) {
    view.visibility = if (isVisibility) View.VISIBLE else View.GONE
}
