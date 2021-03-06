package com.io.movies.bind.adapter

import android.graphics.Color
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.databinding.BindingAdapter
import com.io.movies.R
import java.text.SimpleDateFormat
import java.util.*


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

@BindingAdapter("app:setDescriptionSharingFromImage")
fun setDescriptionSharingFromImage(motionLayout: MotionLayout, isLoadBackImage: Boolean) {
    motionLayout.apply {
        if (!isLoadBackImage) {
            setTransition(R.id.collapsed, R.id.expanded)
        } else {
            setTransition(R.id.expanded, R.id.collapsed)
        }
    }
}
