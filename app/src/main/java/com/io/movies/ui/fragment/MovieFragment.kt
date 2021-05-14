package com.io.movies.ui.fragment

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.io.movies.R
import com.io.movies.adapter.RecyclerAdapterCompany
import com.io.movies.adapter.RecyclerAdapterCredit
import com.io.movies.app.App
import com.io.movies.databinding.FragmentMovieBinding
import com.io.movies.ui.activity.IMovie
import com.io.movies.ui.activity.MainActivity
import com.io.movies.util.Config
import javax.inject.Inject

class MovieFragment: Fragment() {

    companion object {
        private const val ID = "id"
        private const val IS_FAVORITE = "isFavorite"

        fun newInstanceBundle(id: Int, isFavorite: Boolean): Bundle = Bundle().apply {
                    putInt(ID, id)
                    putBoolean(IS_FAVORITE, isFavorite)
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

        (activity as MainActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.let {
                it.setDisplayHomeAsUpEnabled(true)
                it.setDisplayShowHomeEnabled(true)
            }

            binding.toolbar.navigationIcon = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_arrow_back_24, null)!!
        }

        requireArguments().getInt(ID).let {
            binding.viewmodel?.load(it)
        }

        liveDataListener()

        binding.imdb.setOnClickListener {
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("${Config.imdb}${binding.movie?.imdb}")
                    )
                )
            } catch (e: ActivityNotFoundException){
                Log.e("Error","In MovieFragment in time click site - ${e.message}")
            }
        }

        binding.motionLayout.setTransitionListener(object : TransitionAdapter() {
            override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
                when (currentId){
                    R.id.expanded -> {

                    }
                    R.id.collapsed -> {

                    }
                }
            }
        })

        binding.site.setOnClickListener {
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(binding.movie?.homepage)))
            } catch (e: ActivityNotFoundException){
                Log.e("Error","In MovieFragment in time click imdb - ${e.message}")
            }
        }

        var isFavorite = requireArguments().getBoolean(IS_FAVORITE)
        binding.favorite.apply {
            isSelected = isFavorite
            setOnClickListener {
                isFavorite = !isFavorite
                isSelected = isFavorite
                binding.movie?.id?.let { id -> binding.viewmodel?.updateMovie(id = id, isFavorite = isFavorite) }
            }
        }

    }

    private fun liveDataListener(){
        binding.viewmodel?.isNotLoad!!.set(true)
        binding.viewmodel?.updateMovie?.observe(viewLifecycleOwner) {
           binding.viewmodel?.isNotLoad!!.set(false)
            binding.apply {
                movie = it
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

                countriesRecalculation.apply {
                    it.countries.forEach { value ->
                        (LayoutInflater.from(context)
                            .inflate(R.layout.genres, binding.content, false) as TextView).apply {
                            text = value.country
                            id = View.generateViewId()
                            binding.content.addView(this)
                            addView(this)
                        }
                    }
                }
            }
        }

        binding.viewmodel?.updateCredit?.observe(viewLifecycleOwner){
            binding.actors.adapter = RecyclerAdapterCredit(it.cast)
        }

    }


}