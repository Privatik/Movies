package com.io.movies.ui.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.io.movies.adapter.PagedAdapterMovie
import com.io.movies.app.App
import com.io.movies.databinding.FragmentListBinding
import javax.inject.Inject

class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater,container,false)

        binding.viewmodel = ViewModelProvider(this, factory).get(ListViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PagedAdapterMovie()
        binding.moviesRecycler.adapter = adapter
        binding.viewmodel!!.newsList.observe(viewLifecycleOwner, {
               adapter.submitList(it)
        })
    }
}