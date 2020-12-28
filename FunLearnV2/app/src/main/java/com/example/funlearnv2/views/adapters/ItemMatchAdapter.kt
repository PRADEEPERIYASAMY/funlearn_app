package com.example.funlearnv2.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.funlearnv2.databinding.ItemMatchBinding

class ItemMatchAdapter(val shuffledList: MutableList<String>, val names: List<String>) : RecyclerView.Adapter<ItemMatchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemMatchBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.matchText.text = names[shuffledList[position].toInt()]
    }

    override fun getItemCount(): Int = shuffledList.size

    class ViewHolder(val binding: ItemMatchBinding) : RecyclerView.ViewHolder(binding.root)
}
