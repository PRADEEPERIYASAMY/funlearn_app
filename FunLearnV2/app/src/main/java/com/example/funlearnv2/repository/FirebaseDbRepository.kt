package com.example.funlearnv2.repository

import com.example.funlearnv2.FirebaseSource.FirebaseDbSource
import com.example.funlearnv2.repository.models.FirebaseDbModels
import com.example.funlearnv2.repository.models.Result
import kotlinx.coroutines.delay
import javax.inject.Inject

class FirebaseDbRepository @Inject constructor(
    private val FireStoreSource: FirebaseDbSource
) {
    suspend fun getAlphabetNamesAndImages(): Result<FirebaseDbModels> = try {
        delay(1000)
        Result.build { FireStoreSource.fetchFirebaseDbData() }
    } catch (e: Exception) {
        Result.build<FirebaseDbModels> { throw Exception("Error") }
    }
}
