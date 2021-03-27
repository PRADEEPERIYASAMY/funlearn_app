package com.example.funlearnv2.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.funlearnv2.databinding.ItemSearchBinding
import com.example.funlearnv2.models.Messages

class ItemSearchAdapter : RecyclerView.Adapter<ItemSearchAdapter.ViewHolder>(), Filterable {

    private var publicChatList = ArrayList<Messages>()
    private var messagesList = ArrayList<Messages>()
    private var messagesListFiltered = ArrayList<Messages>()
    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            ItemSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.messages.text = messagesListFiltered!![position].message_description
    }

    override fun getItemCount(): Int = messagesListFiltered.size

    fun setList(messages: Messages) {
        if (messagesList.size> 0 && messagesList[messagesList.size - 1].id == messages.id) return
        messagesList.add(messages)
    }

    /*fun setList(list: ArrayList<Messages>) {
        if (messagesList == null) {
            messagesList = list
            messagesListFiltered = list
            notifyItemChanged(0, messagesListFiltered!!.size)
        } else {
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int = this@ItemSearchAdapter.messagesList!!.size

                override fun getNewListSize(): Int = messagesList!!.size

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                    (
                        this@ItemSearchAdapter.messagesList!![oldItemPosition] == messagesList!![newItemPosition]
                        )

                override fun areContentsTheSame(
                    oldItemPosition: Int,
                    newItemPosition: Int
                ): Boolean {
                    val newMovie = this@ItemSearchAdapter.messagesList!![oldItemPosition]

                    val oldMovie = messagesList!![newItemPosition]

                    return newMovie.id === oldMovie.id
                }
            })
        }
    }*/

    class ViewHolder(val binding: ItemSearchBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString()
                messagesListFiltered = if (charString.isEmpty()) {
                    messagesList
                } else {
                    val filteredList = ArrayList<Messages>()
                    for (message in messagesList) {
                        if (message.message_description!!.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(message)
                        }
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = messagesListFiltered
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                messagesListFiltered = results!!.values as ArrayList<Messages>
                notifyDataSetChanged()
            }
        }
    }
}
