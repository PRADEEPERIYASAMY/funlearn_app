package com.example.funlearnv2.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel<Action> : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.IO

    protected val mutableError = MutableLiveData<String>()
    val error: LiveData<String>
        get() = mutableError

    protected val mutableSuccess = MutableLiveData<String>()
    val success: LiveData<String>
        get() = mutableSuccess

    protected var mutableProgress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean>
        get() = mutableProgress

    abstract fun doFireStoreAction(action: Action): Any
}
