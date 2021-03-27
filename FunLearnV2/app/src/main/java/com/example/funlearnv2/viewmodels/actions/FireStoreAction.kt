package com.example.funlearnv2.viewmodels.actions

import androidx.lifecycle.LifecycleOwner
import com.example.funlearnv2.models.Comments
import com.example.funlearnv2.models.Messages
import com.example.funlearnv2.models.Users

sealed class FireStoreAction {
    data class CreateUser(val users: Users) : FireStoreAction()
    data class CreateMessage(val messages: Messages) : FireStoreAction()
    data class CreateComment(val comments: Comments) : FireStoreAction()
    object FetchPublicMessages : FireStoreAction()
}
