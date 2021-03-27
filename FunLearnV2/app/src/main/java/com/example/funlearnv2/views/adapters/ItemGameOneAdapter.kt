package com.example.funlearnv2.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.funlearnv2.databinding.ItemGameOneBinding
import com.example.funlearnv2.views.widgets.GameImages

class ItemGameOneAdapter(level:Int) : RecyclerView.Adapter<ItemGameOneAdapter.ViewHolder>() {

    private var mutableSelectedBox: MutableLiveData<Int> = MutableLiveData()
    val selectedBox: LiveData<Int> get() = mutableSelectedBox
    private lateinit var context:Context

    private val gameObjectImages = GameImages.images.shuffled()
    private val boxNoPair = mutableListOf<Pair<Int,Int>>()
    private val boxNo = mutableListOf<Int>()
    private val boxSizes = listOf(Pair(4,2),Pair(4,3),Pair(4,4),Pair(4,5),Pair(4,6),Pair(5,6))
    private val boxSize = boxSizes[level].first*boxSizes[level].second
    init {
        for (i in 0 until boxSize/2) boxNoPair.add(Pair(i,i))
        boxNoPair.shuffle()
        for (i in 0 until boxSize/2) {
            val pair = Pair(boxNoPair[i].first,i)
            boxNoPair.removeAt(i)
            boxNoPair.add(i,pair)
        }
        boxNoPair.shuffle()
        for (i in  0 until boxSize/2) {
            boxNo.add(boxNoPair[i].first)
            boxNo.add(boxNoPair[i].second)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(ItemGameOneBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(gameObjectImages[boxNo[position]]).into(holder.binding.imageObject)
        holder.binding.root.setOnClickListener { mutableSelectedBox.postValue(position) }
    }

    override fun getItemCount(): Int = boxSize

    class ViewHolder(val binding: ItemGameOneBinding) : RecyclerView.ViewHolder(binding.root)
}