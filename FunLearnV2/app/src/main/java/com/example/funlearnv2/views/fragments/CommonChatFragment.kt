package com.example.funlearnv2.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.funlearnv2.databinding.FragmentCommonChatBinding
import com.example.funlearnv2.models.Messages
import com.example.funlearnv2.utils.toGone
import com.example.funlearnv2.utils.toVisible
import com.example.funlearnv2.viewmodels.FireStoreViewModel
import com.example.funlearnv2.views.adapters.ItemSearchAdapter
import com.example.funlearnv2.views.adapters.chatViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class CommonChatFragment : Fragment() {

    private var _binding: FragmentCommonChatBinding? = null
    private val binding
        get() = _binding!!

    private val itemSearchAdapter = ItemSearchAdapter()
    private lateinit var viewModel: FireStoreViewModel
    private var publicChatList = arrayListOf<Messages>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommonChatBinding.inflate(inflater, container, false)
        initViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.chatViewPager.adapter = chatViewPagerAdapter(requireContext(), childFragmentManager)
        binding.chatTabLayout.setupWithViewPager(binding.chatViewPager)
        initSearchView()
    }

    private fun initTabWatcher() {
        binding.chatTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab!!.position) {
                    binding.chatTabLayout[0].id -> {
                        Toast.makeText(requireContext(), "1", Toast.LENGTH_SHORT).show()
                    }
                    binding.chatTabLayout[1].id -> {
                        Toast.makeText(requireContext(), "2", Toast.LENGTH_SHORT).show()
                    }
                    binding.chatTabLayout[2].id -> {
                        Toast.makeText(requireContext(), "3", Toast.LENGTH_SHORT).show()
                    }
                    binding.chatTabLayout[3].id -> {
                        Toast.makeText(requireContext(), "4", Toast.LENGTH_SHORT).show()
                    }
                    else -> Toast.makeText(requireContext(), "none", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun initSearchView() {
        binding.searchRecyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            hasFixedSize()
            adapter = itemSearchAdapter
        }

        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                itemSearchAdapter.filter.filter(query)
                binding.searchRecyclerview.toVisible()
                binding.chatViewPager.toGone()
                binding.chatTabLayout.toGone()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                itemSearchAdapter.filter.filter(newText)
                binding.searchRecyclerview.toVisible()
                binding.chatViewPager.toGone()
                binding.chatTabLayout.toGone()
                return false
            }
        })
        binding.searchBar.setOnSearchClickListener {
            binding.searchRecyclerview.toVisible()
            binding.chatViewPager.toGone()
            binding.chatTabLayout.toGone()
        }
        binding.searchBar.setOnCloseListener {
            binding.searchRecyclerview.toGone()
            binding.chatViewPager.toVisible()
            binding.chatTabLayout.toVisible()
            true
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(requireActivity()).get(FireStoreViewModel::class.java)
        /*if (viewModel.messages.value == null) {
            viewModel.doAction(FireStoreAction.FetchPublicMessages(viewLifecycleOwner))
        }*/
    }

    @ExperimentalCoroutinesApi
    private fun observeMessages() {
        viewModel.fetchMessages.observe(
            viewLifecycleOwner,
            {
                itemSearchAdapter.setList(it)
                itemSearchAdapter.notifyDataSetChanged()
            }
        )
    }

    @ExperimentalCoroutinesApi
    override fun onResume() {
        super.onResume()
        observeMessages()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
