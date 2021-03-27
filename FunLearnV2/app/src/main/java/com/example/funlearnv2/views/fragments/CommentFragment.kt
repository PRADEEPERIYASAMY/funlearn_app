package com.example.funlearnv2.views.fragments

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.funlearnv2.databinding.FragmentCommentBinding
import com.example.funlearnv2.models.*
import com.example.funlearnv2.utils.toGone
import com.example.funlearnv2.utils.toVisible
import com.example.funlearnv2.viewmodels.FireStoreViewModel
import com.example.funlearnv2.viewmodels.actions.FireStoreAction
import com.example.funlearnv2.views.adapters.ItemCommentAdapter
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.vanniktech.emoji.EmojiPopup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class CommentFragment : Fragment() {

    private var _binding: FragmentCommentBinding? = null
    private val binding
        get() = _binding!!

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private lateinit var viewModel: FireStoreViewModel
    private var commentsList = arrayListOf<Comments>()
    private val itemCommentAdapter = ItemCommentAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommentBinding.inflate(inflater, container, false)
        initViewModel()
        initCommentRecycler()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initOnClick()
        initTextWatcher()
    }

    private fun initTextWatcher() {
        binding.inputMessage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s!!.isEmpty()) binding.buttonSend.toGone()
                else binding.buttonSend.toVisible()
            }
        })
    }

    private fun initOnClick() {
        binding.buttonSend.setOnClickListener {
            createComment(binding.inputMessage.text.toString())
        }
        binding.buttonVoiceText.setOnClickListener {
            promptSpeechInput()
        }

        val popup = EmojiPopup.Builder.fromRootView(binding.rootView).build(binding.inputMessage)
        binding.buttonEmoji.setOnClickListener {
            popup.toggle()
        }
    }

    private fun createComment(comment: String) {
        viewModel.doAction(
            FireStoreAction.CreateComment(
                Comments(
                    id = "${firebaseAuth.currentUser!!.uid}------${System.currentTimeMillis()}",
                    message_id = viewModel.mutableMessageId.value,
                    from = firebaseAuth.currentUser!!.uid,
                    from_name = "",
                    comment = comment,
                    timeStamp = Timestamp.now()
                )
            )
        )
    }

    private fun promptSpeechInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(
            RecognizerIntent.EXTRA_PROMPT,
            "tell something"
        )
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT)
        } catch (a: ActivityNotFoundException) {
            Toast.makeText(
                context,
                "not supported",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(requireActivity()).get(FireStoreViewModel::class.java)
        viewModel.mutableMessageId.value = arguments?.getString("message_id")
    }

    private fun initCommentRecycler() {
        binding.commentRecyclerView.apply {
            adapter = itemCommentAdapter
            layoutManager = LinearLayoutManager(context)
            hasFixedSize()
        }
    }

    @ExperimentalCoroutinesApi
    private fun observeComment() {
        viewModel.fetchComments.observe(
            viewLifecycleOwner,
            {
                itemCommentAdapter.setList(it)
                itemCommentAdapter.notifyDataSetChanged()
                binding.commentRecyclerView.scrollToPosition(itemCommentAdapter.itemCount - 1)
            }
        )
    }

    @ExperimentalCoroutinesApi
    override fun onResume() {
        super.onResume()
        observeComment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val REQ_CODE_SPEECH_INPUT = 100
    }
}
