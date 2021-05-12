package com.io.movies.ui.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.io.movies.R

class LoadDialog: DialogFragment(R.layout.dialog_load) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window!!.setBackgroundDrawableResource(android.R.color.transparent)
    }
}