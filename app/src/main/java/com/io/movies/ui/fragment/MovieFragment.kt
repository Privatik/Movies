package com.io.movies.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.io.movies.R
import com.io.movies.adapter.RecyclerAdapterCompany
import com.io.movies.app.App
import com.io.movies.databinding.FragmentMovieBinding
import com.io.movies.ui.activity.IMovie
import javax.inject.Inject

class MovieFragment: Fragment() {

    companion object {
        private const val ID = "id"

        fun newInstanceBundle(id: Int): Bundle = Bundle().apply {
                    putInt(ID, id)
                }
    }

    private lateinit var binding: FragmentMovieBinding

    private lateinit var aboutMovie: IMovie

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    override fun onAttach(context: Context) {
        super.onAttach(context)

        aboutMovie = context as IMovie

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMovieBinding.inflate(inflater, container, false)
        binding.viewmodel = ViewModelProvider(this, factory).get(MovieViewModel::class.java)

        return binding.root
    }

    @SuppressLint("InflateParams")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireArguments().getInt(ID).let {
            binding.viewmodel?.load(it)
        }

        binding.viewmodel?.updateMovie?.observe(viewLifecycleOwner) {
            binding.apply {
                movie = it
                scrollView.visibility = View.VISIBLE
                aboutMovie.closeDialogLoadAboutMovie()
                company.adapter = RecyclerAdapterCompany(it.companies)

                genres.apply {

                    it.genres.forEach { value ->
                        (LayoutInflater.from(context)
                            .inflate(R.layout.genres, binding.content, false) as TextView).apply {
                            text = value.name
                            id = View.generateViewId()
                            binding.content.addView(this)
                            addView(this)
                        }
                    }
                }
            }
        }

    }
}