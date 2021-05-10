package com.io.movies.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.io.movies.databinding.DialogMovieBinding

class MovieDialog: DialogFragment() {

    private lateinit var binding: DialogMovieBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DialogMovieBinding.inflate(inflater, container, false)
        binding.viewmodel = ViewModelProvider(this).get(MovieViewModel::class.java)
        return binding.root
    }
}