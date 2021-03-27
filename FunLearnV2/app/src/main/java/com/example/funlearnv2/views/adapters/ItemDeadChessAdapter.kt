package com.example.funlearnv2.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.funlearnv2.databinding.ItemDeadChessBinding

class ItemDeadChessAdapter : RecyclerView.Adapter<ItemDeadChessAdapter.ViewHolder>() {

    val chessPieceDeadList = mutableListOf<ItemDeadChessBinding>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemDeadChessBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        chessPieceDeadList.add(position,holder.binding)
    }

    override fun getItemCount(): Int = 16

    class ViewHolder(val binding: ItemDeadChessBinding) : RecyclerView.ViewHolder(binding.root)
}