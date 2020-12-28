package com.example.funlearnv2.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.funlearnv2.R
import com.example.funlearnv2.databinding.FragmentParentVerificationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ParentVerificationFragment : Fragment() {

    private var _binding: FragmentParentVerificationBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentParentVerificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initOnclick()
    }

    private fun initOnclick() {
        binding.statusButton.setOnClickListener {
            findNavController().navigate(R.id.action_parentVerificationFragment_to_parentActivity)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
