package com.io.movies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.io.movies.databinding.ItemCompanyBinding
import com.io.movies.model.Company

class RecyclerAdapterCompany(private val list: List<Company>): RecyclerView.Adapter<RecyclerAdapterCompany.ItemCompany>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemCompany = ItemCompany(
        ItemCompanyBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ItemCompany, position: Int) {
        holder.binding.company = list[position]
    }

    override fun getItemCount(): Int = list.size

    class ItemCompany(val binding: ItemCompanyBinding): RecyclerView.ViewHolder(binding.root){

    }
}