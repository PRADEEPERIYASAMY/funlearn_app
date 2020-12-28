package com.example.funlearnv2.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.funlearnv2.views.adapters.chatViewPagerAdapter
import com.example.funlearnv2.databinding.FragmentCommonChatBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommonChatFragment : Fragment() {

    private var _binding: FragmentCommonChatBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommonChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.chatViewPager.adapter = chatViewPagerAdapter(requireContext(), parentFragmentManager)
        binding.chatTabLayout.setupWithViewPager(binding.chatViewPager)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
