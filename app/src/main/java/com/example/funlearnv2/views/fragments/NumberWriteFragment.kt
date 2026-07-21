package com.example.funlearnv2.views.fragments

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.funlearnv2.R
import com.example.funlearnv2.databinding.FragmentNumberWriteBinding
import com.example.funlearnv2.utils.constants.ButtonStatus
import com.example.funlearnv2.utils.constants.Constant
import com.example.funlearnv2.utils.toInvisible
import com.example.funlearnv2.utils.toVisible
import com.example.funlearnv2.viewmodels.FirebaseDbViewModel
import com.example.funlearnv2.viewmodels.actions.FirebaseDbAction
import com.example.funlearnv2.views.adapters.ItemOperatorAdapter
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import dagger.hilt.android.AndroidEntryPoint
import pl.droidsonroids.gif.GifDrawable

@AndroidEntryPoint
class NumberWriteFragment : Fragment() {

    private var _binding: FragmentNumberWriteBinding? = null
    private val binding
        get() = _binding!!

    private var index = 0
    private lateinit var viewModel: FirebaseDbViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNumberWriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.numberWrite.setColorBackground(ContextCompat.getColor(requireContext(),R.color.grey))
        initViewModel()
        initOnClick()
    }

    private fun initOnClick() {
        binding.buttonSubmit.setOnClickListener {
            when (binding.buttonSubmit.text.toString()) {
                ButtonStatus.SUBMIT.type -> {
                    binding.buttonSubmit.text = ButtonStatus.NEXT.type
                    initDetect()
                }
                ButtonStatus.NEXT.type -> {
                    binding.buttonSubmit.text = ButtonStatus.SUBMIT.type
                    binding.resultRecyclerview.toInvisible()
                    binding.numberWrite.clear(binding.numberWrite.bitmap)
                    binding.numberWrite.setColorBackground(ContextCompat.getColor(requireContext(),R.color.grey))
                }
            }
        }
    }

    private fun initDetect() {
        val image = InputImage.fromBitmap(binding.numberWrite.bitmap, 0)
        val recognizer = TextRecognition.getClient()
        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                val blockList = visionText.textBlocks
                if (blockList.isEmpty()) {
                    binding.resultGif.toVisible()
                    (binding.resultGif.drawable as GifDrawable).start()
                    (binding.resultGif.drawable as GifDrawable).reset()
                    binding.result.text = "Wrong"
                    Handler(Looper.getMainLooper()).postDelayed(
                        {
                            Runnable { (binding.resultGif.drawable as GifDrawable).stop() }
                        },
                        ((binding.resultGif.drawable as GifDrawable).duration - 300).toLong()
                    )
                } else {
                    for (block in visionText.textBlocks) {
                        val string: String = block.text
                        if ((binding.numberTask.text.toString() == string)) {
                            binding.resultGif.toInvisible()
                            binding.result.text = Constant.numberNames[index]
                            resultSetter()
                        } else {
                            binding.resultGif.toVisible()
                            (binding.resultGif.drawable as GifDrawable).start()
                            (binding.resultGif.drawable as GifDrawable).reset()
                            binding.result.text = "Wrong"
                            Handler(Looper.getMainLooper()).postDelayed(
                                {
                                    Runnable { (binding.resultGif.drawable as GifDrawable).stop() }
                                },
                                ((binding.resultGif.drawable as GifDrawable).duration - 300).toLong()
                            )
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
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
    }

    private fun resultSetter() {
        binding.resultRecyclerview.toVisible()
        binding.resultRecyclerview.apply {
            adapter = viewModel.firebaseDbLiveData.value?.OperatorImages?.let { ItemOperatorAdapter(it, index + 1) }
            layoutManager = GridLayoutManager(requireContext(), 10)
            setHasFixedSize(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
