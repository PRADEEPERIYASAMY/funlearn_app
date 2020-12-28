package com.example.funlearnv2.views.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.funlearnv2.views.adapters.ItemPublicChatMessageAdapter
import com.example.funlearnv2.repository.models.Comments
import com.example.funlearnv2.repository.models.Message
import com.example.funlearnv2.utils.FileUtil
import com.example.funlearnv2.databinding.FragmentPublicChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PublicChatFragment : Fragment() {

    private var _binding: FragmentPublicChatBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentPublicChatBinding.inflate(inflater, container, false)
        return binding.root
    }
    private val publicChatList = ArrayList<Message>()
    private var chosedFile = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.postRecyclerView.adapter = ItemPublicChatMessageAdapter(publicChatList, findNavController())
        initOnclick()
        initFetchListener()
        /*binding.postRecyclerView.apply {
            adapter = ItemPublicChatMessageAdapter(object :InterfaceSendMessageOnClick{
                override fun sendMessage(type: String, messageContent: String, messageDescription: String) {

                }
            })
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
        }*/
    }

    private fun initFetchListener() {
        publicChatList.clear()
        FirebaseDatabase.getInstance().reference.child("Chat/PublicChat").addChildEventListener(
            object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    if (snapshot.exists()) {
                        snapshot.children.forEach {
                            publicChatList.add(it.getValue<Message>()!!)
                            binding.postRecyclerView.adapter!!.notifyDataSetChanged()
                            binding.postRecyclerView.scrollToPosition(publicChatList.size - 1)
                        }
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onCancelled(error: DatabaseError) {
                }
            }
        )
    }

    private fun initOnclick() {
        binding.sendMessageButton.setOnClickListener {
            val time = System.currentTimeMillis()
            val message = Message(
                MessageContent = "",
                MessageDescription = binding.inputMessage.text.toString(),
                Type = "txt",
                Downloaded = "No",
                Time = time.toString(),
                PostBy = FirebaseAuth.getInstance().currentUser!!.uid,
                CommentsCount = 0.toString(),
                LikesCount = 0.toString(),
                DisLikesCount = 0.toString(),
                Comments = arrayListOf(Comments("", "", "", "", ""))
            )

            FirebaseDatabase.getInstance().reference.child("Chat/PublicChat/${FirebaseAuth.getInstance().currentUser!!.uid}-PublicChat-$time/Message")
                .setValue(message).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(requireContext(), "baked", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        binding.sendFilesButton.setOnClickListener {
            if (binding.postOption.visibility == View.GONE) binding.postOption.visibility = View.VISIBLE else binding.postOption.visibility = View.GONE
        }
        binding.sendOthersButton.setOnClickListener {
            askPermissionAndBrowseFile()
        }
    }

    private fun askPermissionAndBrowseFile() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permisson = ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            if (permisson != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    MY_REQUEST_CODE_PERMISSION
                )
                return
            }
        }
        doBrowseFile()
    }

    private fun doBrowseFile() {
        var chooseFileIntent = Intent(Intent.ACTION_GET_CONTENT)
        chooseFileIntent.type = "*/*"
        chooseFileIntent.addCategory(Intent.CATEGORY_OPENABLE)
        chooseFileIntent = Intent.createChooser(chooseFileIntent, "Choose a file")
        startActivityForResult(chooseFileIntent, MY_RESULT_CODE_FILECHOOSER)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions!!, grantResults)
        when (requestCode) {
            MY_REQUEST_CODE_PERMISSION -> {

                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this.context, "Permission granted!", Toast.LENGTH_SHORT).show()
                    doBrowseFile()
                } else {
                    Toast.makeText(this.context, "Permission denied!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            MY_RESULT_CODE_FILECHOOSER -> if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    val fileUri: Uri? = data.data
                    var filePath: String? = null
                    try {
                        filePath = FileUtil.getPath(requireContext(), fileUri!!)
                    } catch (e: Exception) {
                        Toast.makeText(this.context, "Error: $e", Toast.LENGTH_SHORT).show()
                    }
                    chosedFile = filePath!!
                    getThumbnail(filePath)
                    uploadFile(fileUri.toString(), filePath)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun uploadFile(selectedUri: String, selectedPath: String) {
        var fileType: String? = null
        fileType = when (true) {
            selectedPath.endsWith(".png"), selectedPath.endsWith(".jpg"), selectedPath.endsWith(".jpeg") -> "png"
            selectedPath.endsWith(".gif") -> "gif"
            selectedPath.endsWith(".pdf") -> "pdf"
            selectedPath.endsWith(".ppt") -> "ppt"
            selectedPath.endsWith(".mp4") -> "mp4"
            selectedPath.endsWith(".mp3") -> "mp3"
            selectedPath.endsWith(".docx") -> "docx"
            selectedPath.endsWith(".apk") -> "apk"
            else -> "null"
        }
        val time = System.currentTimeMillis()
        val firebaseStorageReference = FirebaseStorage.getInstance().reference.child("FunLearn/Chat/Public/$fileType/${FirebaseAuth.getInstance().currentUser!!.uid}-$time.$fileType")
        val uploadTask = firebaseStorageReference.putFile(Uri.parse(selectedUri)).addOnCompleteListener {
            if (it.isSuccessful) {
                val result = it.result.metadata!!.reference!!.downloadUrl
                result.addOnSuccessListener { uri ->
                    val message = Message(
                        MessageContent = uri.toString(),
                        MessageDescription = binding.inputMessage.text.toString(),
                        Type = fileType,
                        Downloaded = "No",
                        Time = time.toString(),
                        PostBy = FirebaseAuth.getInstance().currentUser!!.uid,
                        CommentsCount = 0.toString(),
                        LikesCount = 0.toString(),
                        DisLikesCount = 0.toString(),
                        Comments = arrayListOf(Comments("", "", "", "", ""))
                    )

                    FirebaseDatabase.getInstance().reference.child("Chat/PublicChat/${FirebaseAuth.getInstance().currentUser!!.uid}-PublicChat-$time/Message")
                        .setValue(message).addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(requireContext(), "baked", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(requireContext(), "failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
        }
    }

    private fun getThumbnail(selectedImageUri: String) {
        val THUMBSIZE = 128

        val thumbImage = ThumbnailUtils.extractThumbnail(
            BitmapFactory.decodeFile(selectedImageUri),
            THUMBSIZE,
            THUMBSIZE
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val MY_REQUEST_CODE_PERMISSION = 1000
        private const val MY_RESULT_CODE_FILECHOOSER = 1001
    }
}

interface InterfaceSendMessageOnClick {
    abstract fun sendMessage(type: String, messageContent: String, messageDescription: String)
}
