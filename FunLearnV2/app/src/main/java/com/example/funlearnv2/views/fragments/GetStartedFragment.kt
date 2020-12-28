package com.example.funlearnv2.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.funlearnv2.R
import com.example.funlearnv2.databinding.FragmentGetStartedBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GetStartedFragment : Fragment() {

    private var _binding: FragmentGetStartedBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGetStartedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initOnclick()
    }

    private fun initOnclick() {
        binding.signUpButton.setOnClickListener {
            findNavController().navigate(R.id.action_getStartedFragment_to_phoneVerificationFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
