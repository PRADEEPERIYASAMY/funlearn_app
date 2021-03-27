package com.example.funlearnv2.utils

import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.getString(): String = this.editText!!.text.toString().trim()
