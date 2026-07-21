package com.example.funlearnv2.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnackbar(
    msgId: Int,
    length: Int,
    actionMessageId: Int,
    action: (View) -> Unit = {}
) {
    showSnackbar(context.getString(msgId), length, context.getString(actionMessageId), action)
}

fun View.showSnackbar(
    msg: String,
    length: Int,
    actionMessage: CharSequence? = null,
    action: (View) -> Unit = {}
) {
    val snackbar = Snackbar.make(this, msg, length)
    if (actionMessage != null) {
        snackbar.setAction(actionMessage) {
            action(this)
        }.show()
    } else if (msg.isNotEmpty() && msg.isNotBlank()) {
        snackbar.show()
    }
}
