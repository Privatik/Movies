package com.io.movies.ui.fragment

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.helper.widget.Flow
import androidx.constraintlayout.widget.ConstraintLayout
import com.io.movies.R
import com.io.movies.adapter.RecyclerAdapterCompany
import com.io.movies.adapter.RecyclerAdapterCredit
import com.io.movies.model.AboutMovie
import com.io.movies.util.Config
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class MovieFragment: BaseMovieFragment() {

    companion object {
        private const val ID = "id"
        private const val IS_FAVORITE = "isFavorite"

        fun setAndGetBundle(id: Int, isFavorite: Boolean): Bundle = Bundle().apply {
                    putInt(ID, id)
                    putBoolean(IS_FAVORITE, isFavorite)
                }
    }

    private var loadDisposable: Disposable? = null

    @SuppressLint("InflateParams")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        loadDisposable = Observable.timer(500, TimeUnit.MILLISECONDS).subscribe{
            if (viewModel.loadAboutMovie.value == null)
            viewModel.load(requireArguments().getInt(ID))
        }
    }

    private fun initAboutMovie(aboutMovie: AboutMovie) {
        binding.apply {
            movie = aboutMovie.also {
                this@MovieFragment.viewModel.apply {
                    (it.backdrop != null).let { isHaveBackImage ->
                        isLoadBackImage.set(isHaveBackImage)
                        setIsClickBackFromToolbar(!isHaveBackImage)
                    }
                }

                company.adapter = RecyclerAdapterCompany(it.companies)

                genres.addTextViews(it.genres.map { genres -> genres.name }, content)
                countriesRecalculation.addTextViews(it.countries.map { country -> country.country }, content)

                binding.favorite.apply {
                    isSelected = requireArguments().getBoolean(IS_FAVORITE)

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
        binding.unbind()
      //  viewModel.clear()
        loadDisposable?.dispose()
        super.onDestroyView()
    }
}

fun Flow.addTextViews(titles: List<String>, constraintLayout: ConstraintLayout){
    titles.forEach {
        (LayoutInflater.from(context)
            .inflate(R.layout.genres, constraintLayout, false) as TextView).apply {
            text = it
            id = View.generateViewId()
            constraintLayout.addView(this)
            addView(this)
        }
    }
}