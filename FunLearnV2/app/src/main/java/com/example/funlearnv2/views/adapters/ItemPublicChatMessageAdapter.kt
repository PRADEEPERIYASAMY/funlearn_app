package com.example.funlearnv2.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.funlearnv2.databinding.ItemPublicPostBinding
import com.example.funlearnv2.models.MessageTypes
import com.example.funlearnv2.models.Messages
import com.example.funlearnv2.utils.toGone
import com.example.funlearnv2.views.fragments.CommonChatFragmentDirections

class ItemPublicChatMessageAdapter() : RecyclerView.Adapter<ItemPublicChatMessageAdapter.ViewHolder>() {

    private var publicChatList = ArrayList<Messages>()
    private lateinit var navController: NavController

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemPublicPostBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (publicChatList[position].message_type == MessageTypes.TEXT.type) holder.binding.postImage.toGone()
        holder.binding.postText.text = publicChatList[position].message_description
    }

    fun setList(list: Messages) {
        if (publicChatList.size> 0 && publicChatList[publicChatList.size - 1].id == list.id) return
        this.publicChatList.add(list)
        notifyDataSetChanged()
    }

    fun setNavController(navController: NavController) {
        this.navController = navController
    }

    override fun getItemId(position: Int): Long = super.getItemId(position)

    override fun getItemCount(): Int = publicChatList.size

    inner class ViewHolder(val binding: ItemPublicPostBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.comment.setOnClickListener {
                val action = CommonChatFragmentDirections.actionNavChatToCommentFragment(publicChatList[adapterPosition].id!!)
                navController.navigate(action)
            }
        }
    }
}
