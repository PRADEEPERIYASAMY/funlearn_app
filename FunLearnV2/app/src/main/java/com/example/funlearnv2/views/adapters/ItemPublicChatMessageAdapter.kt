package com.example.funlearnv2.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.funlearnv2.R
import com.example.funlearnv2.repository.models.Message
import com.example.funlearnv2.databinding.ItemPublicPostBinding
import com.example.funlearnv2.views.fragments.CommonChatFragmentDirections

class ItemPublicChatMessageAdapter(/*sendClick: InterfaceSendMessageOnClick,*/ private val publicChatList: ArrayList<Message>, private val navController: NavController) : RecyclerView.Adapter<ItemPublicChatMessageAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(ItemPublicPostBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(publicChatList[position].Type){
            "txt"->holder.binding.filesContent.visibility = View.GONE
            "png"->{
                Glide.with(context).load(publicChatList[position].MessageContent).into(holder.binding.filesThumbnail)
                holder.binding.filesContent.visibility = View.VISIBLE
                holder.binding.fileDownloadButton.visibility = View.GONE
            }
            else ->{
               /* holder.binding.filesThumbnail.setImageResource(R.drawable.pdficon)*/
                holder.binding.filesContent.visibility = View.VISIBLE
            }
        }

        holder.binding.userId.text = publicChatList[position].PostBy
        holder.binding.userPostTime.text = publicChatList[position].Time
        holder.binding.messageContent.text = publicChatList[position].MessageDescription
        holder.binding.userActionComment.text = publicChatList[position].CommentsCount
        holder.binding.userActionLike.text = publicChatList[position].LikesCount
        holder.binding.userActionDislike.text = publicChatList[position].DisLikesCount
        holder.binding.userActionComment.setOnClickListener {
            val action = CommonChatFragmentDirections.actionNavChatToCommentFragment("hello")
            navController.navigate(action)
            navController.navigate(R.id.action_nav_Chat_to_commentFragment)
        }
    }

    override fun getItemId(position: Int): Long = super.getItemId(position)

    override fun getItemCount(): Int = publicChatList.size

    class ViewHolder(val binding: ItemPublicPostBinding) : RecyclerView.ViewHolder(binding.root)
}
