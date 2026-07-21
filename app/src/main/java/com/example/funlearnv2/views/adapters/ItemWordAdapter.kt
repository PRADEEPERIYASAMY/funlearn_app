package com.example.funlearnv2.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.funlearnv2.models.AlphabetWords
import com.example.funlearnv2.databinding.FragmentAlphabetWordsBinding
import com.example.funlearnv2.databinding.ItemAlphabetWordsBinding

class ItemWordAdapter(val binding: FragmentAlphabetWordsBinding, val alphabetWords: AlphabetWords) : RecyclerView.Adapter<ItemWordAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemAlphabetWordsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.root.setOnClickListener {
            binding.alphabetWordExampleRecyclerview.apply {
                adapter = ItemWordExampleAdapter(alphabetWords, position + 1)
                layoutManager = GridLayoutManager(context, 2)
                setHasFixedSize(true)
            }
        }
    }

    override fun getItemCount(): Int = alphabetWords.Images!!.size - 1

    class ViewHolder(val binding: ItemAlphabetWordsBinding) : RecyclerView.ViewHolder(binding.root)
}
