package com.example.funlearnv2.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.funlearnv2.databinding.FragmentAlphabetListBinding
import com.example.funlearnv2.viewmodels.FirebaseDbViewModel
import com.example.funlearnv2.viewmodels.actions.FirebaseDbAction
import com.example.funlearnv2.views.adapters.ItemAlphabetAdapter
import com.example.funlearnv2.views.adapters.ItemAlphabetWordAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlphabetListFragment : Fragment() {

    private var _binding: FragmentAlphabetListBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var viewModel: FirebaseDbViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlphabetListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        binding.alphabetListRecyclerview.adapter = viewModel.firebaseDbLiveData.value?.AlphabetImagesAndNames?.let { ItemAlphabetAdapter(binding, it) }
        binding.alphabetStatus.setOnClickListener {
            binding.alphabetListRecyclerview.visibility = when (it.tag) {
                "up" -> {
                    binding.alphabetStatus.text = "up"
                    it.tag = "down"
                    View.GONE
                }
                "down" -> {
                    binding.alphabetStatus.text = "down"
                    it.tag = "up"
                    View.VISIBLE
                }
                else -> View.GONE
            }
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(requireActivity()).get(FirebaseDbViewModel::class.java)
        if (viewModel.firebaseDbLiveData.value == null) {
            viewModel.doAction(FirebaseDbAction.FetchFirebaseDbData)
        }
        viewModel.firebaseDbLiveData.observe(
            viewLifecycleOwner,
            {
                initWordsRecyclerView()
            }
        )
        initWordsRecyclerView()
    }

    private fun initWordsRecyclerView() {
        binding.alphabetWordRecyclerview.apply {
            adapter = viewModel.firebaseDbLiveData.value?.AlphabetImagesAndNames?.let { ItemAlphabetWordAdapter(it, 1) }
            layoutManager = GridLayoutManager(requireContext(), 2)
            setHasFixedSize(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
