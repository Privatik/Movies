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
import com.io.movies.delegate.argument
import com.io.movies.ui.activity.IBackFromAboutMovie
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
    private lateinit var backFromAboutMovie: IBackFromAboutMovie

    private val viewModel by lazy {
        ViewModelProvider(this, factory).get(MovieViewModel::class.java)
    }

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val id = id
        aboutMovie = context as IMovie
        backFromAboutMovie = context as IBackFromAboutMovie
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
        backFromAboutMovie.backButtonClickable(isClickable = false)
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

        Log.e("TAG","isNotLoad -> ${viewModel.isNotLoad.get()}")

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
                        backFromAboutMovie.backButtonClickable(isClickable = true)
                    }
                    R.id.expanded -> {
                        backFromAboutMovie.backButtonClickable(isClickable = false)
                    }
                }
            }
        })

        requireArguments().apply {
            viewModel.load(getInt(ID))
            liveDataListener(getBoolean(IS_FAVORITE))
        }

        binding.mainContent.imdb.setOnClickListener {
            try { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("${Config.imdb}${binding.movie?.imdb}"))) }
            catch (e: ActivityNotFoundException){ Log.e("Error","In MovieFragment in time click site - ${e.message}") }
        }

        binding.mainContent.site.setOnClickListener {
            try { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(binding.movie?.homepage))) }
            catch (e: ActivityNotFoundException){ Log.e("Error","In MovieFragment in time click imdb - ${e.message}") }
        }

    }

    private fun liveDataListener(isFavorite: Boolean) {
        viewModel.isNotLoad.set(true)

        viewModel.updateMovie.observe(viewLifecycleOwner) {
            binding.apply {
                this@MovieFragment.viewModel.isNotLoad.set(false)
                this@MovieFragment.viewModel.isLoadBackImage.set(it.backdrop != null)
                movie = it
                aboutMovie.closeDialogLoadAboutMovie()
                mainContent.apply {
                    company.adapter = RecyclerAdapterCompany(it.companies)

                    genres.addTextViews(it.genres.map { genres -> genres.name }, content)
                    countriesRecalculation.addTextViews(
                        it.countries.map { country -> country.country },
                        content
                    )
                }

                mainContent.favorite.apply {
                    isSelected = isFavorite

                    setOnClickListener { view ->
                        isSelected = !isSelected

                        this@MovieFragment.viewModel.updateMovie(
                            aboutMovie = it,
                            isFavorite = isSelected
                        )
                    }
                }
            }

            viewModel.updateCredit.observe(viewLifecycleOwner) {
                binding.mainContent.actors.adapter = RecyclerAdapterCredit(it.cast)
            }

        }
    }
}

fun Flow.addTextViews(titles: List<String>, content: ConstraintLayout){
    titles.forEach {
        (LayoutInflater.from(context)
            .inflate(R.layout.genres, content, false) as TextView).apply {
            text = it
            id = View.generateViewId()
            content.addView(this)
            addView(this)
        }
    }
}