package com.io.movies.ui.dialog

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.io.movies.R
import com.io.movies.app.App
import com.io.movies.ui.activity.IDialog
import com.io.movies.ui.fragment.MovieFragment
import javax.inject.Inject

class LoadDialog: DialogFragment(R.layout.dialog_load) {

    private lateinit var dialogController: IDialog

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProvider(this, factory).get(LoadViewModel::class.java)
    }

    companion object {
        private const val ID = "id"
        private const val IS_FAVORITE = "isFavorite"

        fun newInstance(id: Int, isFavorite: Boolean): LoadDialog =
            LoadDialog().apply {
                arguments = Bundle().apply {
                    putInt(ID, id)
                    putBoolean(IS_FAVORITE, isFavorite)
                }
            }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        dialogController = context as IDialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
         setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window!!.setBackgroundDrawableResource(android.R.color.transparent)


        requireArguments().apply {
            viewModel.load(id = getInt(ID))

            viewModel.updateMovie.observe(viewLifecycleOwner){
                dialogController.closeDialogLoadAboutMovie(MovieFragment.setAndGetBundle(it, getBoolean(IS_FAVORITE)))
            }
        }
    }
}