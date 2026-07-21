package com.example.funlearnv2.utils

import android.content.DialogInterface
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Fragment.showEditTextDialog(onPositiveClick: (String) -> Unit) {
    val editText = EditText(this.requireContext())
    editText.hint = "Details"
    editText.maxLines = 8

    MaterialAlertDialogBuilder(this.requireContext())
        .setTitle("New Social Awareness Activity")
        .setView(editText)
        .setNegativeButton("Cancel", null)
        .setPositiveButton("OK") { _: DialogInterface, _: Int ->
            if (editText.text.toString().isNotBlank())
                onPositiveClick(editText.text.toString())
        }.show()
}
