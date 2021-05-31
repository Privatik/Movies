package com.io.movies.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.io.movies.R
import com.io.movies.app.App
import com.io.movies.databinding.FragmentMovieBinding
import com.io.movies.ui.activity.MainActivity
import javax.inject.Inject

open class BaseMovieFragment: Fragment() {

    protected lateinit var binding: FragmentMovieBinding

    protected val viewModel by lazy {
        ViewModelProvider(this, factory).get(MovieViewModel::class.java)
    }

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private var isClickableBackButtonInMovieFragment = true

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.let {
                it.setDisplayHomeAsUpEnabled(true)
                it.setDisplayShowHomeEnabled(true)
            }

            binding.toolbar.navigationIcon = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_arrow_back_24, null)!!
            binding.toolbar.setNavigationOnClickListener {
                if (isClickableBackButtonInMovieFragment) {
                    activity?.onBackPressed()
                }
            }
        }

        binding.motionLayout.setTransitionListener(object : TransitionAdapter(){
            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                when (currentId){
                    R.id.collapsed -> {
                        Log.e("Motion","anim collapsed")
                        isClickableBackButtonInMovieFragment = true
                    }
                    R.id.expanded -> {
                        Log.e("Motion","anim expanded")
                        isClickableBackButtonInMovieFragment = false
                    }
                    else -> {
                        Log.e("Motion","anim else")
                    }
                }
            }
        })
    }

    fun setIsClickBackFromToolbar(isClick: Boolean){
        isClickableBackButtonInMovieFragment = isClick
    }
}