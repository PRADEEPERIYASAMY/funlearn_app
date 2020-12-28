package com.example.funlearnv2.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.funlearnv2.repository.models.Comments
import com.example.funlearnv2.databinding.ItemCommentPostBinding

class ItemPublicCommentMessageAdapter(/*sendClick: InterfaceSendMessageOnClick,*/ private val publicCommentList: ArrayList<Comments>) : RecyclerView.Adapter<ItemPublicCommentMessageAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(ItemCommentPostBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.userId.text = publicCommentList[position].PostBy
        holder.binding.userPostTime.text = publicCommentList[position].Time
        holder.binding.messageContent.text = publicCommentList[position].MessageContent
        holder.binding.userActionLike.text = publicCommentList[position].LikesCount
        holder.binding.userActionDislike.text = publicCommentList[position].DisLikesCount
    }

    override fun getItemId(position: Int): Long = super.getItemId(position)

    override fun getItemCount(): Int = publicCommentList.size

    class ViewHolder(val binding: ItemCommentPostBinding) : RecyclerView.ViewHolder(binding.root)
}
