package com.io.movies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.io.movies.databinding.ItemCompanyBinding
import com.io.movies.databinding.ItemCreditBinding
import com.io.movies.model.Company
import com.io.movies.model.Credit

class RecyclerAdapterCredit(private val list: List<Credit>): RecyclerView.Adapter<RecyclerAdapterCredit.ItemCredit>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemCredit = ItemCredit(
        ItemCreditBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ItemCredit, position: Int) {
        holder.binding.credit = list[position]
    }

    override fun getItemCount(): Int = list.size

    class ItemCredit(val binding: ItemCreditBinding): RecyclerView.ViewHolder(binding.root){

    }
}