package com.example.funlearnv2.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.funlearnv2.databinding.ItemNumbersInfoBinding
import com.example.funlearnv2.utils.constants.Constant

class ItemNumbersInfoAdapter() : RecyclerView.Adapter<ItemNumbersInfoAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(ItemNumbersInfoBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.number.text = (position+1).toString()
        holder.binding.numberInWords.text = Constant.numberNames[position]
    }

    override fun getItemCount(): Int = 100

    class ViewHolder(val binding: ItemNumbersInfoBinding) : RecyclerView.ViewHolder(binding.root)
}
