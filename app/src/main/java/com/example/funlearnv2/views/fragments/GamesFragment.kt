package com.example.funlearnv2.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.funlearnv2.R
import com.example.funlearnv2.databinding.FragmentGamesBinding
import com.example.funlearnv2.utils.constants.FunType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GamesFragment : Fragment() {

    private var _binding: FragmentGamesBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGamesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initOnClick()
    }

    private fun initOnClick() {
        binding.alphabetCard.setOnClickListener {
            val action = GamesFragmentDirections.actionNavGameToFunOptionsFragment(FunType.ALPHABETS)
            findNavController().navigate(action)
        }
        binding.mathCard.setOnClickListener {
            val action = GamesFragmentDirections.actionNavGameToFunOptionsFragment(FunType.MATHS)
            findNavController().navigate(action)
        }
        binding.paintCard.setOnClickListener {
            val action = GamesFragmentDirections.actionNavGameToFunOptionsFragment(FunType.PAINT)
            findNavController().navigate(action)
        }
        binding.gameCard.setOnClickListener {
            val action = GamesFragmentDirections.actionNavGameToFunOptionsFragment(FunType.GAME)
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
