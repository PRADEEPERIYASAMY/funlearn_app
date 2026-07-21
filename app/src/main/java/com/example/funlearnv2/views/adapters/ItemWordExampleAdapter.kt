package com.example.funlearnv2.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.funlearnv2.models.AlphabetWords
import com.example.funlearnv2.databinding.ItemAlphabetWordsBinding

class ItemWordExampleAdapter(alphabetWords: AlphabetWords, id: Int) : RecyclerView.Adapter<ItemWordExampleAdapter.ViewHolder>() {

    private lateinit var context: Context
    private var images = alphabetWords.Images?.get(id)?.split("----------")!!
    private var words = alphabetWords.Words?.get(id)?.split("----------")!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(ItemAlphabetWordsBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(images[position]).into(holder.binding.itemWordImage)
        holder.binding.itemWordName.text = words[position]
    }

    override fun getItemCount(): Int = 10

    class ViewHolder(val binding: ItemAlphabetWordsBinding) : RecyclerView.ViewHolder(binding.root)
}
