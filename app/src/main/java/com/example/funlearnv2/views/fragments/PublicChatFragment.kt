package com.example.funlearnv2.views.fragments

import android.app.Activity.RESULT_OK
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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.funlearnv2.databinding.FragmentPublicChatBinding
import com.example.funlearnv2.models.MessageTypes
import com.example.funlearnv2.models.Messages
import com.example.funlearnv2.models.Mode
import com.example.funlearnv2.models.Roles
import com.example.funlearnv2.utils.toGone
import com.example.funlearnv2.utils.toVisible
import com.example.funlearnv2.viewmodels.FireStoreViewModel
import com.example.funlearnv2.viewmodels.actions.FireStoreAction
import com.example.funlearnv2.views.adapters.ItemPublicChatMessageAdapter
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.vanniktech.emoji.EmojiPopup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class PublicChatFragment : Fragment() {

    private var _binding: FragmentPublicChatBinding? = null
    private val binding
        get() = _binding!!

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private var chosedFile = ""
    private lateinit var viewModel: FireStoreViewModel
    private var publicChatList = arrayListOf<Messages>()
    private val itemPublicChatMessageAdapter = ItemPublicChatMessageAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentPublicChatBinding.inflate(inflater, container, false)
        initMessageRecycler()
        initViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*observeClickedMessage()*/
        itemPublicChatMessageAdapter.setNavController(findNavController())
        initOnClick()
        initTextWatcher()
    }

    private fun initOnClick() {
        binding.buttonSend.setOnClickListener {
            createPost("", binding.inputMessage.text.toString(), MessageTypes.TEXT)
        }
        binding.buttonVoiceText.setOnClickListener {
            promptSpeechInput()
        }

        val popup = EmojiPopup.Builder.fromRootView(binding.rootView).build(binding.inputMessage)
        binding.buttonEmoji.setOnClickListener {
            popup.toggle()
        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQ_CODE_SPEECH_INPUT -> {
                if (resultCode == RESULT_OK && null != data) {
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    binding.inputMessage.setText(result.toString())
                }
            }
        }
    }

    private fun createPost(
        messageContent: String,
        messageDescription: String,
        messageTypes: MessageTypes
    ) {
        viewModel.doAction(
            FireStoreAction.CreateMessage(
                Messages(
                    id = "${firebaseAuth.currentUser!!.uid}------${System.currentTimeMillis()}",
                    role = Roles.CHILD.type,
                    mode = Mode.PUBLIC.type,
                    from = firebaseAuth.currentUser!!.uid,
                    to = Mode.PUBLIC.type,
                    message_content = messageContent,
                    message_description = messageDescription,
                    timeStamp = Timestamp.now(),
                    message_type = messageTypes.type
                )
            )
        )
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(requireActivity()).get(FireStoreViewModel::class.java)
        /*if (viewModel.messages.value == null) {
            viewModel.doAction(FireStoreAction.FetchPublicMessages(viewLifecycleOwner))
        }*/
    }

    @ExperimentalCoroutinesApi
    private fun observeMessages() {
        /*viewModel.messages.removeObservers(viewLifecycleOwner)
        viewModel.messages.observe(
            viewLifecycleOwner,
            {
                itemPublicChatMessageAdapter.setList(it)
                itemPublicChatMessageAdapter.notifyDataSetChanged()
                binding.postRecyclerView.scrollToPosition(it.size - 1)
            }
        )*/
        viewModel.fetchMessages.observe(
            viewLifecycleOwner,
            {
                itemPublicChatMessageAdapter.setList(it)
                itemPublicChatMessageAdapter.notifyDataSetChanged()
                binding.postRecyclerView.scrollToPosition(itemPublicChatMessageAdapter.itemCount - 1)
            }
        )
    }

    /*private fun observeClickedMessage() {
        itemPublicChatMessageAdapter.clickedMessage.removeObservers(viewLifecycleOwner)
        itemPublicChatMessageAdapter.clickedMessage.observe(
            viewLifecycleOwner,
            {
                val action = CommonChatFragmentDirections.actionNavChatToCommentFragment(it)
                findNavController().navigate(action)
            }
        )
    }*/

    private fun initMessageRecycler() {
        binding.postRecyclerView.apply {
            adapter = itemPublicChatMessageAdapter
            layoutManager = LinearLayoutManager(context)
            hasFixedSize()
        }
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

    companion object {
        private const val REQ_CODE_SPEECH_INPUT = 100
    }
}
