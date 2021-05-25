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
import androidx.constraintlayout.helper.widget.Flow
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.io.movies.R
import com.io.movies.adapter.RecyclerAdapterCompany
import com.io.movies.adapter.RecyclerAdapterCredit
import com.io.movies.app.App
import com.io.movies.databinding.FragmentMovieBinding
import com.io.movies.model.AboutMovie
import com.io.movies.ui.activity.IBackFromAboutMovie
import com.io.movies.ui.activity.MainActivity
import com.io.movies.util.Config
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MovieFragment: Fragment() {

    companion object {
        private const val ID = "id"
        private const val IS_FAVORITE = "isFavorite"

        fun setAndGetBundle(id: Int, isFavorite: Boolean): Bundle = Bundle().apply {
                    putInt(ID, id)
                    putBoolean(IS_FAVORITE, isFavorite)
                }
    }

    private lateinit var binding: FragmentMovieBinding
    private var backButtonFromToolbarFromAboutMovie: IBackFromAboutMovie? = null

    private var loadDisposable: Disposable? = null

    private val viewModel by lazy {
        ViewModelProvider(this, factory).get(MovieViewModel::class.java)
    }

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    override fun onAttach(context: Context) {
        super.onAttach(context)

        backButtonFromToolbarFromAboutMovie = context as IBackFromAboutMovie
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
        binding.viewModel = viewModel

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

        binding.motionLayout.setTransitionListener(object : TransitionAdapter(){
            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                when (currentId){
                    R.id.collapsed -> {
                        Log.e("Motion","anim collapsed")
                        backButtonFromToolbarFromAboutMovie?.backButtonClickable(isClickable = true)
                    }
                    R.id.expanded -> {
                        Log.e("Motion","anim expanded")
                        backButtonFromToolbarFromAboutMovie?.backButtonClickable(isClickable = false)
                    }
                    else -> {
                        Log.e("Motion","anim else")
                    }
                }
            }
        })

        viewModel.loadAboutMovie.observe(viewLifecycleOwner){
            initAboutMovie(it)
        }

        viewModel.updateCredit.observe(viewLifecycleOwner) {
            binding.actors.adapter = RecyclerAdapterCredit(it.cast)
        }

        binding.apply {
            imdb.setOnClickListener {
                try { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("${Config.imdb}${binding.movie?.imdb}"))) }
                catch (e: ActivityNotFoundException){ Log.e("Error","In MovieFragment in time click site - ${e.message}") }
            }

            site.setOnClickListener {
                try { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(binding.movie?.homepage))) }
                catch (e: ActivityNotFoundException){ Log.e("Error","In MovieFragment in time click imdb - ${e.message}") }
            }
        }

<<<<<<< HEAD
        viewModel.load(requireArguments().getInt(ID))
    }

    override fun onStart() {
        super.onStart()
        Log.e("FragmentMovie","OnStart")
    }

    override fun onResume() {
        super.onResume()
        Log.e("FragmentMovie","OnResume")
=======
<<<<<<< Updated upstream
>>>>>>> main
    }

    override fun onDetach() {
        super.onDetach()
        backButtonFromToolbarFromAboutMovie = null
    }

    private fun initAboutMovie(aboutMovie: AboutMovie) {
        binding.apply {
            movie = aboutMovie.also {
                this@MovieFragment.viewModel.apply {
                    (it.backdrop != null).let { isHaveBackImage ->
                        isLoadBackImage.set(isHaveBackImage)
                        backButtonFromToolbarFromAboutMovie?.backButtonClickable(!isHaveBackImage)
                    }
                }

<<<<<<< HEAD
                company.adapter = RecyclerAdapterCompany(it.companies)

                genres.addTextViews(it.genres.map { genres -> genres.name })
                countriesRecalculation.addTextViews(it.countries.map { country -> country.country })

                binding.favorite.apply {
                    isSelected = requireArguments().getBoolean(IS_FAVORITE)
=======
                mainContent.favorite.apply {
                    isSelected = isFavorite
=======
        loadDisposable = Observable.timer(500, TimeUnit.MILLISECONDS).subscribe{
            viewModel.load(requireArguments().getInt(ID))
        }
    }

    private fun initAboutMovie(aboutMovie: AboutMovie) {
        binding.apply {
            movie = aboutMovie.also {
                this@MovieFragment.viewModel.apply {
                    (it.backdrop != null).let { isHaveBackImage ->
                        isLoadBackImage.set(isHaveBackImage)
                        backButtonFromToolbarFromAboutMovie?.backButtonClickable(!isHaveBackImage)
                    }
                }

                company.adapter = RecyclerAdapterCompany(it.companies)

                genres.addTextViews(it.genres.map { genres -> genres.name }, content)
                countriesRecalculation.addTextViews(it.countries.map { country -> country.country }, content)

                binding.favorite.apply {
                    isSelected = requireArguments().getBoolean(IS_FAVORITE)
>>>>>>> Stashed changes
>>>>>>> main

                    setOnClickListener { _ ->
                        isSelected = !isSelected

                        this@MovieFragment.viewModel.updateMovie(
                            aboutMovie = it,
                            isFavorite = isSelected
                        )
                    }
                }
            }
        }

        viewModel.isLoadAboutMovie.set(false)
    }

    override fun onDestroyView() {
        loadDisposable?.dispose()
        super.onDestroyView()
    }



    override fun onDetach() {
        super.onDetach()
        backButtonFromToolbarFromAboutMovie = null
    }
}

<<<<<<< HEAD
fun Flow.addTextViews(titles: List<String>){
=======
<<<<<<< Updated upstream
fun Flow.addTextViews(titles: List<String>, content: ConstraintLayout){
>>>>>>> main
    titles.forEach {
        (LayoutInflater.from(context)
            .inflate(R.layout.genres, parent as ConstraintLayout, false) as TextView).apply {
            text = it
            id = View.generateViewId()
<<<<<<< HEAD
            (parent as ConstraintLayout).addView(this)
=======
            content.addView(this)
=======
fun Flow.addTextViews(titles: List<String>, constraintLayout: ConstraintLayout){
    titles.forEach {
        (LayoutInflater.from(context)
            .inflate(R.layout.genres, constraintLayout, false) as TextView).apply {
            text = it
            id = View.generateViewId()
            constraintLayout.addView(this)
>>>>>>> Stashed changes
>>>>>>> main
            addView(this)
        }
    }
}