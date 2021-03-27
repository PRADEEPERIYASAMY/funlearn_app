package com.example.funlearnv2.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.funlearnv2.databinding.FragmentSignInBinding
import com.example.funlearnv2.R
import com.example.funlearnv2.utils.getString
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding
        get() = _binding!!

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initOnClick()
    }

    private fun initOnClick() {
        binding.signIn.setOnClickListener {
            firebaseAuth.signInWithEmailAndPassword(binding.email.getString(), binding.password.getString())
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        findNavController().navigate(R.id.action_signInFragment_to_userTypeFragment)
                        Toast.makeText(context, "success", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val SignIn_TIME_OUT = 2000
    }
}
