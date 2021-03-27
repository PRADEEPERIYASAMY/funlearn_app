package com.example.funlearnv2.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.funlearnv2.R
import com.example.funlearnv2.databinding.FragmentNumberOperationBinding
import com.example.funlearnv2.utils.constants.ButtonStatus
import com.example.funlearnv2.viewmodels.FirebaseDbViewModel
import com.example.funlearnv2.viewmodels.actions.FirebaseDbAction
import com.example.funlearnv2.views.adapters.ItemOperatorAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlin.collections.ArrayList

@AndroidEntryPoint
class NumberOperationFragment : Fragment() {

    private var _binding: FragmentNumberOperationBinding? = null
    private val binding
        get() = _binding!!
    private var type: Int = 0
    private var firstNum = 0
    private var secondNum = 0
    private var resultNum = 0
    private lateinit var viewModel: FirebaseDbViewModel
    private val operatorImages = listOf(R.drawable.ic_plus, R.drawable.ic_minus, R.drawable.ic_multiply, R.drawable.ic_divide, R.drawable.ic_fillup)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNumberOperationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        type = NumberOperationFragmentArgs.fromBundle(requireArguments()).operatorType.type
        binding.operator.setImageDrawable(ContextCompat.getDrawable(requireContext(), operatorImages[type]))
        initViewModel()
        initOnClick()
    }

    private fun initOnClick() {
        binding.statusButton.setOnClickListener {
            when (binding.statusButton.text.toString()) {
                ButtonStatus.SUBMIT.type -> {
                    if (binding.answer.text.toString() == resultNum.toString()) {
                        binding.statusButton.text = ButtonStatus.NEXT.type
                        resultSetter()
                    } else {
                        binding.answer.setText("")
                    }
                }
                ButtonStatus.NEXT.type -> {
                    binding.statusButton.text = ButtonStatus.SUBMIT.type
                    initValue()
                }
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
            }
        )
        initValue()
    }

    private fun initValue() {
        binding.answer.setText("")
        resultNum = 0
        resultSetter()
        val firstNumList: MutableList<Int> = ArrayList()
        val secondNumList: MutableList<Int> = ArrayList()
        firstNum = 0
        secondNum = 0
        for (i in 1..99) {
            firstNumList.add(i)
        }
        firstNumList.shuffle()
        when (type) {
            0 -> {
                firstNum = firstNumList[(Math.random() * 98).toInt()]
                for (i in firstNumList) {
                    if (i + firstNum <= 100) {
                        secondNumList.add(i)
                    }
                }
            }
            1 -> {
                firstNum = firstNumList[(Math.random() * 98).toInt()]
                for (i in firstNumList) {
                    if (firstNum - i > 0) {
                        secondNumList.add(i)
                    }
                }
            }
            2 -> {
                firstNum = firstNumList[(Math.random() * 30).toInt()]
                for (i in firstNumList) {
                    if (firstNum * i <= 100) {
                        secondNumList.add(i)
                    }
                }
            }
            3 -> {
                firstNum = firstNumList[(Math.random() * 30).toInt() + 68]
                for (i in firstNumList) {
                    if (firstNum % i == 0) {
                        secondNumList.add(i)
                    }
                }
            }
            4 -> {
                firstNumList.clear()
                for (i in 1..98) {
                    firstNumList.add(i)
                }
                firstNumList.shuffle()
                firstNum = firstNumList[(Math.random() * 97).toInt()]
                secondNumList.add(firstNum + 2)
            }
        }
        secondNumList.shuffle()
        secondNum = secondNumList[(Math.random() * (secondNumList.size - 1)).toInt()]
        resultNum = when (type) {
            0 -> firstNum + secondNum
            1 -> firstNum - secondNum
            2 -> firstNum * secondNum
            3 -> firstNum / secondNum
            4 -> firstNum + 1
            else -> 100
        }
        binding.numberOne.text = firstNum.toString()
        binding.numberTwo.text = secondNum.toString()
    }

    private fun resultSetter() {
        binding.operatorResultRecyclerview.apply {
            adapter = viewModel.firebaseDbLiveData.value?.OperatorImages?.let { ItemOperatorAdapter(it, resultNum) }
            layoutManager = GridLayoutManager(requireContext(), 10)
            setHasFixedSize(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
