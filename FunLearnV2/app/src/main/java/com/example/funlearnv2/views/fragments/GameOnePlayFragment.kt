package com.example.funlearnv2.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.funlearnv2.databinding.FragmentGameOnePlayBinding
import com.example.funlearnv2.utils.children
import com.example.funlearnv2.utils.toGone
import com.example.funlearnv2.utils.toVisible
import com.example.funlearnv2.views.adapters.ItemGameOneAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameOnePlayFragment : Fragment() {

    private var _binding: FragmentGameOnePlayBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameOnePlayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initOnclick()
    }

    private fun  initOnclick(){
        for (i in 0..5)
            binding.containerGameOneLevel.children[i].setOnClickListener {
                binding.containerGameOneLevel.toGone()
                binding.containerGameOnePlay.toVisible()
                CURRENT_LEVEL = i
                initRecyclerView(CURRENT_LEVEL)
            }
    }

    private fun initRecyclerView(level:Int){
        binding.recyclerGameOnePlay.apply {
            adapter = ItemGameOneAdapter(level)
            setHasFixedSize(true)
        }
        if (level<5) binding.recyclerGameOnePlay.layoutManager = GridLayoutManager(context,level+2)
        else binding.recyclerGameOnePlay.layoutManager = GridLayoutManager(context,level+1)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        private var CURRENT_LEVEL = 0
    }
}
