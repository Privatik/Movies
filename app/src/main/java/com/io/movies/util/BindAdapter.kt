package com.io.movies.util

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.io.movies.R
import org.jetbrains.annotations.NotNull

@BindingAdapter("app:loadImageOrNoPoster")
fun loadImageOrNoPoster(view: ImageView, url: String?) {
    Glide.with(view.context)
        .load("${Config.photo}${url}")
        .error(R.drawable.no_poster)
        .into(view)
}

@BindingAdapter("app:loadImageOrNoImage")
fun loadImageOrNoImage(view: ImageView, url: String?) {
    Glide.with(view.context)
        .load("${Config.photo}${url}")
        .error(R.drawable.no_image)
        .into(view)
}

@BindingAdapter("app:loadImage")
fun loadImage(view: ImageView, url: String?) {
    Glide.with(view.context)
        .load("${Config.photo}${url}")
        .into(view)
}

@NotNull
@BindingAdapter("app:isVisibility")
fun isVisibility(view: View, isVisibility: Boolean) {
    view.visibility = if (isVisibility) View.VISIBLE else View.GONE
}

