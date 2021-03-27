package com.example.funlearnv2.views.fragments

import android.Manifest
import android.app.KeyguardManager
import android.content.Context.KEYGUARD_SERVICE
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.funlearnv2.R
import com.example.funlearnv2.databinding.FragmentParentVerificationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ParentVerificationFragment : Fragment() {

    private var _binding: FragmentParentVerificationBinding? = null
    private val binding
        get() = _binding!!
    private lateinit var cancellationSignal: CancellationSignal

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentParentVerificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkBiometricSupport()
        initOnClick()
    }

    private fun checkBiometricSupport(): Boolean {
        val keyguardManager = requireActivity().getSystemService(KEYGUARD_SERVICE) as KeyguardManager?

        val packageManager = requireActivity().packageManager

        if (!keyguardManager!!.isKeyguardSecure) {
            Toast.makeText(
                context,
                "Lock screen security not enabled in Settings",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.USE_BIOMETRIC
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(
                context,
                "Fingerprint authentication permission not enabled",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        return if (packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
            true
        } else true
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun authenticationCallback(): BiometricPrompt.AuthenticationCallback {

        return (
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    Toast.makeText(requireContext(), "Authentication error: $errString", Toast.LENGTH_SHORT).show()
                    super.onAuthenticationError(errorCode, errString)
                }

                override fun onAuthenticationHelp(
                    helpCode: Int,
                    helpString: CharSequence
                ) {
                    super.onAuthenticationHelp(helpCode, helpString)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    Toast.makeText(requireContext(), "Authentication Succeeded", Toast.LENGTH_SHORT).show()
                    super.onAuthenticationSucceeded(result)
                    findNavController().navigate(R.id.action_parentVerificationFragment_to_parentActivity)
                }
            }
            )
    }

    private fun getCancellationSignal(): CancellationSignal {

        cancellationSignal = CancellationSignal()
        cancellationSignal.setOnCancelListener {
            Toast.makeText(
                requireContext(),
                "canceled sign",
                Toast.LENGTH_SHORT
            ).show()
        }
        return cancellationSignal
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun authenticateUser() {
        val biometricPrompt = BiometricPrompt.Builder(requireContext())
            .setTitle("Biometric Demo")
            .setSubtitle("Authentication is required to continue")
            .setDescription("This app uses biometric authentication to protect your data.")
            .setNegativeButton(
                "Cancel", requireContext().mainExecutor,
                { _, _ ->
                    Toast.makeText(
                        requireContext(),
                        "Authentication cancelled",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
            .build()
        biometricPrompt.authenticate(
            getCancellationSignal(), requireContext().mainExecutor,
            authenticationCallback()
        )
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun initOnClick() {
        binding.statusButton.setOnClickListener {
            authenticateUser()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
