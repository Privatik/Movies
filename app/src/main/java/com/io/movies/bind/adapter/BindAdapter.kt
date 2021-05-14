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

@BindingAdapter("app:isVisibility")
fun isVisibility(view: View, isNotVisibility: Boolean) {
   // Log.e("IsVisibility","$isNotVisibility")
    view.visibility = if (isNotVisibility) View.GONE else View.VISIBLE
}

@BindingAdapter("app:textDate")
fun textDate(textView: TextView, date: Date?) {
    if (date != null){
        textView.text = SimpleDateFormat("dd MMMM yyyy", Locale.US).format(date)
    } else {
        textView.text = "-"
    }
}

@BindingAdapter("app:textStatus")
fun textColorStatus(textView: TextView, text: String?){
    if (text == null) return
    textView.apply {
        setTextColor(
            when (text) {
                "Rumored" -> Color.BLUE
                "Planned",
                "In Production",
                "Post Production" -> Color.YELLOW
                "Released" -> Color.GREEN
                "Canceled" -> Color.RED
                else -> Color.WHITE
            }
        )
        this.text = text
    }
}

