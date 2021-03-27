package com.example.funlearnv2.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.funlearnv2.databinding.FragmentNumberInfoBinding
import com.example.funlearnv2.views.adapters.ItemNumbersInfoAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NumberInfoFragment : Fragment() {

    private var _binding: FragmentNumberInfoBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNumberInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.numbersRecyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            hasFixedSize()
            adapter = ItemNumbersInfoAdapter()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
