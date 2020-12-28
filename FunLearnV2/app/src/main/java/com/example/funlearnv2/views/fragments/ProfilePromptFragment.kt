package com.example.funlearnv2.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.funlearnv2.R
import com.example.funlearnv2.databinding.FragmentProfilePromptBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfilePromptFragment : Fragment() {

    private var _binding: FragmentProfilePromptBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfilePromptBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initOnclick()
    }

    private fun initOnclick() {
        binding.updateProfileButton.setOnClickListener {
            firebaseCreateUser()
        }
    }

    private fun firebaseCreateUser() {
        var map = hashMapOf<String, String>()
        map["ChildName"] = getString(binding.childName)
        map["Description"] = getString(binding.description)
        map["DateOfBirth"] = getString(binding.dateOfBirth)
        map["Address"] = getString(binding.userAddress)
        map["Gender"] = getString(binding.gender)
        map["ParentPassword"] = getString(binding.parentPassword)
        map["ChildGrade"] = getString(binding.grade)
        map["ChildProfileImage"] = ""
        FirebaseDatabase.getInstance().reference.child("Users/Child/${FirebaseAuth.getInstance().currentUser!!.uid}").setValue(map).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(requireContext(), "updated", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_profileFragment_to_userTypeFragment)
            } else {
                Toast.makeText(requireContext(), "failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getString(textView: TextView): String = textView.text.toString().trim()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
