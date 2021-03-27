package com.example.funlearnv2.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.funlearnv2.databinding.FragmentAlphabetMatchBinding
import com.example.funlearnv2.models.Match
import com.example.funlearnv2.utils.get
import com.example.funlearnv2.viewmodels.FirebaseDbViewModel
import com.example.funlearnv2.viewmodels.actions.FirebaseDbAction
import com.example.funlearnv2.views.adapters.ItemMatchAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AlphabetMatchFragment : Fragment() {

    private var _binding: FragmentAlphabetMatchBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var viewModel: FirebaseDbViewModel
    private val allList = mutableListOf<String>()
    private val selectedList = mutableListOf<String>()
    private val shuffledList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlphabetMatchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        ItemTouchHelper(simpleCallback).attachToRecyclerView(binding.matchAnswerRecyclerview)
    }

    private val simpleCallback = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END, 0) {
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            val d = viewHolder.adapterPosition
            val t = target.adapterPosition
            Collections.swap(shuffledList, d, t)
            recyclerView.adapter!!.notifyDataSetChanged()
            winChecker()
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            // no swipe required
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
                initMatchEngine(it.Match!!)
            }
        )
        viewModel.firebaseDbLiveData.value?.Match?.let {
            initMatchEngine(it!!)
        }
    }

    private fun initMatchEngine(match: Match) {
        val names = match.Names?.split("----------")!!
        val images = match.Images?.split("----------")!!
        allList.clear()
        selectedList.clear()
        shuffledList.clear()
        for (i in names.indices) {
            allList.add(i.toString())
        }
        for (i in 0..4) {
            val index = (Math.random() * allList.size).toInt() - 1
            selectedList.add(allList[index])
            allList.removeAt(index)
        }
        selectedList.shuffle()
        for (i in 0..4) {
            shuffledList.add(selectedList[i])
        }
        shuffledList.shuffle()
        for (i in 0..4) {
            Glide.with(requireContext()).load(images[selectedList[i].toInt()]).into(binding.matchLayout[i] as ImageView)
            binding.matchLayout[i]!!.tag = selectedList[i]
        }
        binding.matchAnswerRecyclerview.adapter = ItemMatchAdapter(shuffledList, names)
    }

    private fun winChecker() {
        val refactorList = mutableListOf<Int>()
        for (i in 0..4) {
            if (selectedList[i] === shuffledList[i]) {
                refactorList.add(1)
            }
        }
        if (refactorList.size == 5) {
            /*val builder = AlertDialog.Builder(requireContext(), R.style.CustomDialog)
            val dialogBinding =  AlphabetWriteDialogBinding.inflate(LayoutInflater.from(requireContext()), null, false)
            dialogBinding.firstText.text = "Well done !"
            dialogBinding.secontText.text = "Wanna move to next ?"
            dialogBinding.dialogImage.setImageResource(R.drawable.excellent)
            val alertDialog = builder.create()
            alertDialog.setView(dialogBinding.root)
            dialogBinding.no.setOnClickListener {

            }
            dialogBinding.yes.setOnClickListener {

            }
            alertDialog.setCanceledOnTouchOutside(false)
            alertDialog.show()*/
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
