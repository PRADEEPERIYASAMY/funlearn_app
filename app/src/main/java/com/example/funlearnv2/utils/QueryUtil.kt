package com.example.funlearnv2.utils

import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

data class QueryResponse(val packet: QuerySnapshot?, val error: FirebaseFirestoreException?)

suspend fun Query.awaitRealtime() = suspendCancellableCoroutine<QueryResponse> { continuation ->
    addSnapshotListener { value, error ->
        if (error == null && continuation.isActive)
            continuation.resume(QueryResponse(value, null))
        else if (error != null && continuation.isActive)
            continuation.resume(QueryResponse(null, error))
    }
}
