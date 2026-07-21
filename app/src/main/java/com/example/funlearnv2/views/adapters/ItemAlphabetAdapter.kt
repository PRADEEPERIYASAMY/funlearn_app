package com.example.funlearnv2.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.funlearnv2.models.AlphabetImagesAndNames
import com.example.funlearnv2.utils.constants.Constant
import com.example.funlearnv2.databinding.FragmentAlphabetListBinding
import com.example.funlearnv2.databinding.ItemAlphabetBinding
import java.util.*

class ItemAlphabetAdapter(val binding: FragmentAlphabetListBinding, val alphabetImagesAndNames: AlphabetImagesAndNames) : RecyclerView.Adapter<ItemAlphabetAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemAlphabetBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.alphabetName.text = "${Constant.alphabets[position].toUpperCase(Locale.ROOT)}${Constant.alphabets[position]}"
        holder.binding.alphabetName.setOnClickListener {
            binding.alphabetWordRecyclerview.apply {
                adapter = ItemAlphabetWordAdapter(alphabetImagesAndNames, position + 1)
                layoutManager = GridLayoutManager(context, 2)
                setHasFixedSize(true)
            }
        }
    }

    override fun getItemCount(): Int = Constant.alphabets.size

    class ViewHolder(val binding: ItemAlphabetBinding) : RecyclerView.ViewHolder(binding.root)
}
