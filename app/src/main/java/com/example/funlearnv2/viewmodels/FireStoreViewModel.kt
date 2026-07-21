package com.example.funlearnv2.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.funlearnv2.models.Comments
import com.example.funlearnv2.models.Messages
import com.example.funlearnv2.models.Result
import com.example.funlearnv2.models.Users
import com.example.funlearnv2.repository.FireStoreRepository
import com.example.funlearnv2.viewmodels.actions.FireStoreAction
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Exception

class FireStoreViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    private val repository: FireStoreRepository
) : BaseViewModel<FireStoreAction>() {

    private val mutableUser = MutableLiveData<Users>()
    val user: LiveData<Users>
        get() = mutableUser

    private val mutableMessages = MutableLiveData<ArrayList<Messages>>()
    val messages: LiveData<ArrayList<Messages>>
        get() = mutableMessages

    var mutableMessageId = MutableLiveData<String>()

    override fun doAction(action: FireStoreAction): Any =
        when (action) {
            is FireStoreAction.CreateUser -> createUser(action.users)
            is FireStoreAction.CreateMessage -> createMessage(action.messages)
            is FireStoreAction.FetchPublicMessages -> fetchMessages()
            is FireStoreAction.CreateComment -> createComment(action.comments)
        }

    private fun createUser(users: Users) = launch {
        when (val result = repository.createUser(users)) {
            is Result.Value -> { setSuccess(result.value) }
        }
    }

    private fun createMessage(messages: Messages) = launch {
        when (val result = repository.createMessage(messages)) {
            is Result.Value -> { setSuccess(result.value) }
        }
    }

    private fun createComment(comments: Comments) = launch {
        when (val result = repository.createComment(comments)) {
            is Result.Value -> { setSuccess(result.value) }
        }
    }

    private fun fetchMessages() = launch {
        /*when (val result = repository.fetchPublicMessages()) {
            is Result.Value -> result.su
        }*/
    }

    @ExperimentalCoroutinesApi
    val fetchMessages = liveData(coroutineContext) {
        try {
            repository.fetchPublicMessages().collect {
                emit(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @ExperimentalCoroutinesApi
    val fetchComments = liveData(coroutineContext) {
        try {
            repository.fetchComments(mutableMessageId.value!!).collect {
                emit(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setUser(users: Users) {
        mutableUser.value = users
    }
}
