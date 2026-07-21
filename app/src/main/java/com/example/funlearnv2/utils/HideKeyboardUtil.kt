package com.example.funlearnv2.utils

import android.app.Activity
import android.view.inputmethod.InputMethodManager

fun Activity.hideKeyBoard() {
    val inputMethodManager : InputMethodManager = applicationContext.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken,0)
}
