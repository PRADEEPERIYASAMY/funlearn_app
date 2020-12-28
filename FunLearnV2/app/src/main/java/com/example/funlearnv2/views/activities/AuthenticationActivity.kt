package com.example.funlearnv2.views.activities

import android.os.Bundle
import com.example.funlearnv2.databinding.ActivityAuthenticationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthenticationActivity : BaseActivity() {

    private var _binding: ActivityAuthenticationBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
