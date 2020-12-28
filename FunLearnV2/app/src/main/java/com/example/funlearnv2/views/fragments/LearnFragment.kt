package com.example.funlearnv2.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.funlearnv2.R
import com.example.funlearnv2.databinding.FragmentLearnBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LearnFragment : Fragment() {

    private var _binding: FragmentLearnBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLearnBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.alpha1.setOnClickListener {
            findNavController().navigate(R.id.action_nav_learn_to_alphabetListFragment)
        }
        binding.alpha2.setOnClickListener {
            findNavController().navigate(R.id.action_nav_learn_to_alphabetMatchFragment)
        }
        binding.alpha3.setOnClickListener {
            findNavController().navigate(R.id.action_nav_learn_to_alphabetWordsFragment)
        }
        binding.num1.setOnClickListener {
            findNavController().navigate(R.id.action_nav_learn_to_numberOperationFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
