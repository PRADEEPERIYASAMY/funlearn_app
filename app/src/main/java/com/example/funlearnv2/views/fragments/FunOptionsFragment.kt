package com.example.funlearnv2.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.funlearnv2.R
import com.example.funlearnv2.databinding.FragmentFunOptionsBinding
import com.example.funlearnv2.utils.constants.FunType
import com.example.funlearnv2.utils.constants.OperatorTypes
import com.example.funlearnv2.utils.toVisible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FunOptionsFragment : Fragment() {

    private var _binding: FragmentFunOptionsBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFunOptionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initOnClick()
    }

    private fun initViews() {
        when (FunOptionsFragmentArgs.fromBundle(requireArguments()).funType.type) {
            FunType.ALPHABETS.type -> binding.alphabetContainer.toVisible()
            FunType.MATHS.type -> binding.numberContainer.toVisible()
            FunType.PAINT.type -> binding.paintContainer.toVisible()
            FunType.GAME.type -> binding.gameContainer.toVisible()
        }
    }

    private fun initOnClick() {
        // alphabets
        binding.alphabetCard1.setOnClickListener { findNavController().navigate(R.id.action_funOptionsFragment_to_alphabetListFragment) }
        binding.alphabetCard2.setOnClickListener { findNavController().navigate(R.id.action_funOptionsFragment_to_alphabetWriteFragment) }
        binding.alphabetCard3.setOnClickListener { findNavController().navigate(R.id.action_funOptionsFragment_to_alphabetWordsFragment) }
        binding.alphabetCard4.setOnClickListener { findNavController().navigate(R.id.action_funOptionsFragment_to_alphabetMatchFragment) }
        binding.alphabetCard5.setOnClickListener { findNavController().navigate(R.id.action_funOptionsFragment_to_alphabetCountFragment) }

        // maths
        binding.numberCard1.setOnClickListener { findNavController().navigate(R.id.action_funOptionsFragment_to_numberInfoFragment) }
        binding.numberCard2.setOnClickListener { findNavController().navigate(R.id.action_funOptionsFragment_to_numberWriteFragment) }
        binding.numberCard3.setOnClickListener { findNavController().navigate(R.id.action_funOptionsFragment_to_numberCountFragment) }
        binding.numberCard4.setOnClickListener {
            val action = FunOptionsFragmentDirections.actionFunOptionsFragmentToNumberOperationFragment(OperatorTypes.ADDITION)
            findNavController().navigate(action)
        }
        binding.numberCard5.setOnClickListener {
            val action = FunOptionsFragmentDirections.actionFunOptionsFragmentToNumberOperationFragment(OperatorTypes.SUBTRACTION)
            findNavController().navigate(action)
        }
        binding.numberCard6.setOnClickListener {
            val action = FunOptionsFragmentDirections.actionFunOptionsFragmentToNumberOperationFragment(OperatorTypes.MULTIPLICATION)
            findNavController().navigate(action)
        }
        binding.numberCard7.setOnClickListener {
            val action = FunOptionsFragmentDirections.actionFunOptionsFragmentToNumberOperationFragment(OperatorTypes.DIVISION)
            findNavController().navigate(action)
        }
        binding.numberCard8.setOnClickListener {
            val action = FunOptionsFragmentDirections.actionFunOptionsFragmentToNumberOperationFragment(OperatorTypes.FILL_UP)
            findNavController().navigate(action)
        }

        // paint
        binding.paintCard1.setOnClickListener { findNavController().navigate(R.id.action_funOptionsFragment_to_colouringOneFragment) }
        binding.paintCard2.setOnClickListener { findNavController().navigate(R.id.action_funOptionsFragment_to_colouringTwoFragment) }
        binding.paintCard3.setOnClickListener { findNavController().navigate(R.id.action_funOptionsFragment_to_colouringThreeFragment) }

        // games
        binding.gameCard1.setOnClickListener { findNavController().navigate(R.id.action_funOptionsFragment_to_gameOnePlayFragment) }
        binding.gameCard2.setOnClickListener { findNavController().navigate(R.id.action_funOptionsFragment_to_gameTwoFragment) }
        binding.gameCard3.setOnClickListener { findNavController().navigate(R.id.action_funOptionsFragment_to_gameThreeFragment) }
        binding.gameCard4.setOnClickListener { findNavController().navigate(R.id.action_funOptionsFragment_to_gameFourFragment) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
