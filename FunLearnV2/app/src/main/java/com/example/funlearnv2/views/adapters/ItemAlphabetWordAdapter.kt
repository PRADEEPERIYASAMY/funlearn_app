package com.example.funlearnv2.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.funlearnv2.models.AlphabetImagesAndNames
import com.example.funlearnv2.databinding.ItemAlphabetWordsBinding

class ItemAlphabetWordAdapter constructor(val alphabetImagesAndNames: AlphabetImagesAndNames, var id: Int) : RecyclerView.Adapter<ItemAlphabetWordAdapter.ViewHolder>() {

    private lateinit var context: Context
    private var images = alphabetImagesAndNames.Images?.get(id)?.split("----------")!!
    private var names = alphabetImagesAndNames.Names?.get(id)?.split("----------")!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(ItemAlphabetWordsBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(images[position]).into(holder.binding.itemWordImage)
        holder.binding.itemWordName.text = names[position]
    }

    override fun getItemCount(): Int = names.size

    class ViewHolder(val binding: ItemAlphabetWordsBinding) : RecyclerView.ViewHolder(binding.root)
}
