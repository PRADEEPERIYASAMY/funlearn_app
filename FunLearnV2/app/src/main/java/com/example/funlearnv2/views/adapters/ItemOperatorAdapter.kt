package com.example.funlearnv2.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.funlearnv2.databinding.ItemOperatorBinding

class ItemOperatorAdapter(operatorImages: String, val count: Int) : RecyclerView.Adapter<ItemOperatorAdapter.ViewHolder>() {

    private lateinit var context: Context
    private val images = operatorImages.split("----------")!!
    private val rand = (Math.random() * 9).toInt()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(ItemOperatorBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(images[rand]).into(holder.binding.operatorImage)
    }

    override fun getItemCount(): Int = count

    class ViewHolder(val binding: ItemOperatorBinding) : RecyclerView.ViewHolder(binding.root)
}
