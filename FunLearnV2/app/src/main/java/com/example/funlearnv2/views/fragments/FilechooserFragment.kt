package com.example.funlearnv2.views.fragments

import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.funlearnv2.repository.models.DataModel
import com.example.funlearnv2.databinding.FragmentFileChooserBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.text.SimpleDateFormat
import javax.inject.Inject

@AndroidEntryPoint
class FilechooserFragment : Fragment() {

    private var _binding: FragmentFileChooserBinding? = null
    private val binding
        get() = _binding!!

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private val fileList = mutableListOf<DataModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentFileChooserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        StrictMode.setVmPolicy(VmPolicy.Builder().build())
        val root = File(requireContext().getExternalFilesDir(null)!!.absolutePath)
        listDir(root)
    }

    private fun listDir(f: File) {
        val files = f.listFiles()
        if (files != null) {
            fileList.clear()
            for (file in files) {
                val currentDate = SimpleDateFormat("dd/MM/yyyy")
                val date: String = currentDate.format(file.lastModified())
                val currentTime = SimpleDateFormat("hh:mm a")
                val time: String = currentTime.format(file.lastModified())
                val dataModel = DataModel()
                dataModel.name = file.name
                dataModel.path = file.absolutePath
                if (file.name.endsWith(".gif") || file.name.endsWith(".tif") || file.name.endsWith(".tiff") || file.name.endsWith(".png") || file.name.endsWith(".jpg") || file.name.endsWith(".jpeg") || file.name.endsWith(".bmp") || file.name.endsWith(".esp") || file.name.endsWith(".raw") || file.name.endsWith(".cr2") || file.name.endsWith(".nef") || file.name.endsWith(".orf") || file.name.endsWith(".sr2")) {
                    dataModel.type = "image"
                } else if (file.name.endsWith(".pdf")) {
                    dataModel.type = "pdf"
                } else if (file.name.endsWith(".ppt") || file.name.endsWith(".pptx")) {
                    dataModel.type = "ppt"
                } else if (file.isDirectory) {
                    dataModel.type = "dir"
                } else if (file.name.endsWith(".mkv") || file.name.endsWith(".mp4") || file.name.endsWith(".m4p") || file.name.endsWith(".mpg") || file.name.endsWith(".mpeg") || file.name.endsWith(".3gp") || file.name.endsWith(".mpe") || file.name.endsWith(".mpv") || file.name.endsWith(".mp2") || file.name.endsWith(".amv") || file.name.endsWith(".asf")) {
                    dataModel.type = "video"
                } else if (file.name.endsWith(".mp3") || file.name.endsWith(".acc") || file.name.endsWith(".ogg") || file.name.endsWith(".wav") || file.name.endsWith(".webm")) {
                    dataModel.type = "audio"
                } else if (file.name.endsWith(".docx")) {
                    dataModel.type = "docx"
                } else if (file.name.endsWith(".apk")) {
                    dataModel.type = "apk"
                } else {
                    dataModel.type = "null"
                }
                try {
                    file.listFiles().size.let {
                        dataModel.type = it.toString()
                    }
                } catch (e: NullPointerException) {
                    dataModel.type = 0.toString()
                }
                dataModel.date = "$date \n $time"
                dataModel.clicked = false
                fileList.add(dataModel)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
