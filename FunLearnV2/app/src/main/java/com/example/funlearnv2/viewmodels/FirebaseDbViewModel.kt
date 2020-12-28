package com.example.funlearnv2.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.example.funlearnv2.viewmodels.actions.FirebaseDbAction
import com.example.funlearnv2.repository.models.FirebaseDbModels
import com.example.funlearnv2.repository.models.Result
import com.example.funlearnv2.repository.FirebaseDbRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class FirebaseDbViewModel @ViewModelInject constructor(
        @Assisted private val savedStateHandle: SavedStateHandle,
        private val repository: FirebaseDbRepository
) : BaseViewModel<FirebaseDbAction>() {

    override val coroutineContext: CoroutineContext
        get()  = Job() + Dispatchers.IO

    private var firebaseDbMutableLiveData = MutableLiveData<FirebaseDbModels>()
    val firebaseDbLiveData : LiveData<FirebaseDbModels>
        get() = firebaseDbMutableLiveData

    override fun doFireStoreAction(action: FirebaseDbAction): Any =
            when(action){
                is FirebaseDbAction.FetchFirebaseDbData -> fetchAlphabetImagesAndName()
            }

    private fun fetchAlphabetImagesAndName() = launch {
        mutableProgress.postValue(true)
        when (val result = repository.getAlphabetNamesAndImages()) {
            is Result.Value -> {
                firebaseDbMutableLiveData.postValue(result.value )
                mutableProgress.postValue(false)
            }
        }
    }
}