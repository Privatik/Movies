package com.io.movies.controller

import android.content.DialogInterface
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.io.movies.R
import com.io.movies.ui.dialog.LoadDialog
import com.io.movies.ui.fragment.MovieFragment

class LoadDialogController(private val myFragmentManager: FragmentManager) {

    private var dialog: LoadDialog? = null

    fun openDialogLoadAboutMovie() {

        dialog = LoadDialog().apply {
            show(myFragmentManager, "AboutMovie")
            onCancel(object : DialogInterface {
                override fun cancel() {   }

                override fun dismiss() {
                    myFragmentManager.popBackStack()
                }

            })
        }
    }

    fun closeDialogLoadAboutMovie() {
        dialog?.let {
            it.dismiss()
            null
        }
    }
}