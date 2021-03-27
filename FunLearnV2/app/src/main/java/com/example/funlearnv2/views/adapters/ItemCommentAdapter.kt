package com.example.funlearnv2.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.funlearnv2.databinding.ItemCommentPostBinding
import com.example.funlearnv2.models.Comments

class ItemCommentAdapter() : RecyclerView.Adapter<ItemCommentAdapter.ViewHolder>() {

    private var commentList = ArrayList<Comments>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemCommentPostBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.commentMessage.text = commentList[position].comment
    }

    fun setList(list: Comments) {
        if (commentList.size> 0 && commentList[commentList.size - 1].id == list.id) return
        this.commentList.add(list)
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long = super.getItemId(position)

    override fun getItemCount(): Int = commentList.size

    inner class ViewHolder(val binding: ItemCommentPostBinding) : RecyclerView.ViewHolder(binding.root)
}
