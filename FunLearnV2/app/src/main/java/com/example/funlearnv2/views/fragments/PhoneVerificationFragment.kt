package com.example.funlearnv2.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.funlearnv2.R
import com.example.funlearnv2.databinding.FragmentPhoneVerificationBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class PhoneVerificationFragment : Fragment() {

    private var _binding: FragmentPhoneVerificationBinding? = null
    private val binding
        get() = _binding!!

    private var storedVerificationId: String? = null
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null

    @Inject
    lateinit var phoneAuthProvider: PhoneAuthProvider
    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPhoneVerificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initOnclick()
    }

    private fun initOnclick() {
        firebaseAuth.firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber("+911111111111","12345")
        firebaseAuth.firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber("+912222222222","12345")
        firebaseAuth.firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber("+913333333333","12345")
        firebaseAuth.firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber("+914444444444","12345")
        firebaseAuth.firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber("+915555555555","12345")
        firebaseAuth.firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber("+916666666666","12345")
        firebaseAuth.firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber("+917777777777","12345")
        firebaseAuth.firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber("+918888888888","12345")
        firebaseAuth.firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber("+919999999999","12345")
        firebaseAuth.firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber("+911212121212","12345")

        binding.optButton.setOnClickListener {
            when (it.tag) {
                "send" -> {
                    binding.optButton.text = "verify"
                    binding.optButton.tag = "verify"
                    phoneAuthProvider.verifyPhoneNumber(
                        binding.mobileNumber.text.toString(),
                        60,
                        TimeUnit.SECONDS,
                        requireActivity(),
                        callback
                    )
                }
                "verify" -> {
                    findNavController().navigate(R.id.action_phoneVerificationFragment_to_userTypeFragment)
                }
            }
        }
    }

    private val callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
            Log.d(TAG, "onVerificationCompleted:$phoneAuthCredential")
            val code = phoneAuthCredential.smsCode
            code.let {
                binding.receivedOpt.setText(code)
                signInWithPhoneAuthCredential(phoneAuthCredential)
            }
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Log.w(TAG, "onVerificationFailed", e)
            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                // ...
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                // ...
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
            }

            // show ui update
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(verificationId, token)
            Log.d(TAG, "onCodeSent:$verificationId")
            Toast.makeText(requireContext(), "code sent", Toast.LENGTH_SHORT).show()
            // Save verification ID and resending token so we can use them later
            storedVerificationId = verificationId
            resendToken = token
        }

        override fun onCodeAutoRetrievalTimeOut(p0: String) {
            super.onCodeAutoRetrievalTimeOut(p0)
        }
    }

    private fun signInWithPhoneAuthCredential(phoneAuthCredential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    val user = it.result?.user
                    Toast.makeText(requireContext(), user.toString(), Toast.LENGTH_SHORT).show()
                    // ...
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", it.exception)
                    if (it.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "PHONE_VERIFICATION"
    }
}
