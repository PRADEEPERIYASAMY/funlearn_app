package com.example.funlearnv2.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.funlearnv2.views.Widgets.Patterns
import com.example.funlearnv2.views.adapters.ItemPatternAdapter
import com.example.funlearnv2.child.engines.PatternEngine
import com.example.funlearnv2.databinding.FragmentColouringThreeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ColouringThreeFragment : Fragment() {

    private var _binding: FragmentColouringThreeBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentColouringThreeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView(binding.patternTask, 25, Patterns.pattern)
        initRecyclerView(binding.colorPalate, 5, Patterns.palate)
        val gridLayoutManager = GridLayoutManager(requireContext(), 5)
        binding.patternSolution.apply {
            layoutManager = gridLayoutManager
            setHasFixedSize(true)
            adapter = ItemPatternAdapter(25, Patterns.taskColor)
        }
        val patternEngine = PatternEngine(binding.patternTask, binding.patternSolution, binding.colorPalate)
        for (i in 0..24) {
            binding.patternSolution.layoutManager!!.getChildAt(i)!!.setOnClickListener {
                patternEngine.taskClickListener(i)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecyclerView(recyclerView: RecyclerView, size: Int, intArray: IntArray) {
        recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 5)
            setHasFixedSize(true)
            adapter = ItemPatternAdapter(size, intArray)
        }
    }
}
