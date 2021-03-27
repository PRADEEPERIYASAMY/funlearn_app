package com.example.funlearnv2.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.example.funlearnv2.models.FirebaseDbModels
import com.example.funlearnv2.models.Result
import com.example.funlearnv2.repository.FirebaseDbRepository
import com.example.funlearnv2.viewmodels.actions.FirebaseDbAction
import kotlinx.coroutines.launch

class FirebaseDbViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    private val repository: FirebaseDbRepository
) : BaseViewModel<FirebaseDbAction>() {

    private var firebaseDbMutableLiveData = MutableLiveData<FirebaseDbModels>()
    val firebaseDbLiveData: LiveData<FirebaseDbModels>
        get() = firebaseDbMutableLiveData

    override fun doAction(action: FirebaseDbAction): Any =
        when (action) {
            is FirebaseDbAction.FetchFirebaseDbData -> fetchAlphabetImagesAndName()
        }

    private fun fetchAlphabetImagesAndName() = launch {
        when (val result = repository.getAlphabetNamesAndImages()) {
            is Result.Value -> {
                firebaseDbMutableLiveData.postValue(result.value)
            }
        }
    }
}
