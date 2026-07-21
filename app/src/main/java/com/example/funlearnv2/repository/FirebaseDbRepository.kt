package com.example.funlearnv2.repository

import com.example.funlearnv2.FirebaseSource.FirebaseDbSource
import com.example.funlearnv2.models.FirebaseDbModels
import com.example.funlearnv2.models.Result
import kotlinx.coroutines.delay
import javax.inject.Inject

class FirebaseDbRepository @Inject constructor(
    private val firebaseDbSource: FirebaseDbSource
) {
    suspend fun getAlphabetNamesAndImages(): Result<FirebaseDbModels> = try {
        delay(1000) // for showing progress bar
        Result.build { firebaseDbSource.fetchFirebaseDbData() }
    } catch (e: Exception) {
        Result.build { throw Exception("Error") }
    }
}
