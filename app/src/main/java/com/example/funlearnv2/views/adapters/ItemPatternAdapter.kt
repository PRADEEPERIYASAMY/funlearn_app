package com.example.funlearnv2.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.funlearnv2.databinding.ItemColorBinding

class ItemPatternAdapter(private val size: Int, private val pattern: IntArray) : RecyclerView.Adapter<ItemPatternAdapter.ViewHolder>() {

    var viewHolderList = mutableListOf<ViewHolder>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemColorBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        viewHolderList.add(holder)
        holder.binding.color.setCardBackgroundColor(pattern[position])
    }

    override fun getItemCount(): Int = size

    class ViewHolder(val binding: ItemColorBinding) : RecyclerView.ViewHolder(binding.root)
}