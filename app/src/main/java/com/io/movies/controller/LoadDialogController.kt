package com.io.movies.controller

import android.util.Log
import androidx.fragment.app.FragmentManager
import com.io.movies.ui.dialog.LoadDialog

class LoadDialogController(private val myFragmentManager: FragmentManager) {

    private var dialog: LoadDialog? = null

    fun openDialogLoadAboutMovie(id: Int, isFavorite: Boolean) {
        Log.e("LoadDialog","open")

        dialog = LoadDialog.newInstance(id = id, isFavorite = isFavorite).apply {
            show(myFragmentManager, "AboutMovie")
        }
    }

    fun closeDialogLoadAboutMovie() {
        Log.e("LoadDialog","dismiss")
        dialog?.let {
            it.dismiss()
            null
        }
    }
}