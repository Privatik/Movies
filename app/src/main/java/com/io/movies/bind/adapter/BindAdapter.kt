package com.io.movies.bind.adapter

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.io.movies.R
import com.io.movies.util.Config
import java.text.SimpleDateFormat
import java.util.*

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

@BindingAdapter("app:isVisibilityOrGone")
fun iisVisibilityOrGone(view: View, isNotVisibility: Boolean) {
   // Log.e("IsVisibility","$isNotVisibility")
    view.visibility = if (isNotVisibility) View.GONE else View.VISIBLE
}

<<<<<<< HEAD
<<<<<<< HEAD
=======
<<<<<<< Updated upstream
=======
>>>>>>> parent of e4ab3c6 (update work app)
@BindingAdapter("app:isLookIt")
fun isLookIt(view: View, isNotVisibility: Boolean) {
    // Log.e("IsVisibility","$isNotVisibility")
    view.alpha = if (isNotVisibility) 0f else 1f
}

>>>>>>> main
