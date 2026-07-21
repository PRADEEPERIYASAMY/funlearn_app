package com.example.funlearnv2.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.funlearnv2.views.adapters.ItemWordAdapter
import com.example.funlearnv2.views.adapters.ItemWordExampleAdapter
import com.example.funlearnv2.viewmodels.actions.FirebaseDbAction
import com.example.funlearnv2.viewmodels.FirebaseDbViewModel
import com.example.funlearnv2.databinding.FragmentAlphabetWordsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlphabetWordsFragment : Fragment() {

    private var _binding: FragmentAlphabetWordsBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var viewModel: FirebaseDbViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlphabetWordsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        binding.alphabetStatus.setOnClickListener {
            binding.alphabetWordListRecyclerview.visibility = when (it.tag) {
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
        binding.alphabetWordListRecyclerview.adapter = viewModel.firebaseDbLiveData.value?.AlphabetWords?.let { ItemWordAdapter(binding, it) }
        binding.alphabetWordExampleRecyclerview.apply {
            adapter = viewModel.firebaseDbLiveData.value?.AlphabetWords?.let { ItemWordExampleAdapter(it, 1) }
            layoutManager = GridLayoutManager(requireContext(), 2)
            setHasFixedSize(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
