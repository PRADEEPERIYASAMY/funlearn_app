package com.example.funlearnv2.repository

import com.example.funlearnv2.FirebaseSource.Collections
import com.example.funlearnv2.FirebaseSource.FireStoreSource
import com.example.funlearnv2.models.Comments
import com.example.funlearnv2.models.Messages
import com.example.funlearnv2.models.Result
import com.example.funlearnv2.models.Users
import com.example.funlearnv2.utils.resourceProvider.ResourceProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.lang.Exception
import javax.inject.Inject

class FireStoreRepository @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val fireStoreSource: FireStoreSource,
    private val dataStoreRepository: DataStoreRepository
) {
    suspend fun createUser(users: Users): Result<String> = try {

        // caching user data
        dataStoreRepository.storeId(users.id!!)
        dataStoreRepository.storeChildGrade(users.child_grade)
        dataStoreRepository.storeChildName(users.child_name!!)
        dataStoreRepository.storeChildProfileImage(users.child_profile_image!!)
        dataStoreRepository.storeChildOnlineVisibility(users.child_online_visibility)
        dataStoreRepository.storeChildOnline(users.child_online)
        dataStoreRepository.storeChildMail(users.child_mail!!)
        dataStoreRepository.storeChildDob(users.child_dob!!)
        dataStoreRepository.storeChildGender(users.child_gender!!)
        dataStoreRepository.storeChildDescription(users.child_description!!)
        dataStoreRepository.storeParentName(users.parent_name!!)
        dataStoreRepository.storeParentDob(users.parent_dob!!)
        dataStoreRepository.storeParentGender(users.parent_gender!!)
        dataStoreRepository.storeParentGrade(users.parent_grade)
        dataStoreRepository.storeParentOnlineVisibility(users.parent_online_visibility)
        dataStoreRepository.storeParentOnline(users.parent_online)
        dataStoreRepository.storeParentProfileImage(users.parent_profile_image!!)
        dataStoreRepository.storeParentMail(users.parent_mail!!)
        dataStoreRepository.storeAccountPassword(users.account_password!!)
        dataStoreRepository.storeAccountMail(users.account_mail!!)
        dataStoreRepository.storeMobileNumber(users.mobile_number!!)
        dataStoreRepository.storeAddress(users.address!!)
        dataStoreRepository.storeScore(users.score.toString())
        dataStoreRepository.storeCash(users.cash.toString())

        Result.build { fireStoreSource.create(users, Collections.USERS) }
    } catch (e: Exception) {
        Result.build { throw Exception(e.message.toString()) }
    }

    suspend fun createMessage(messages: Messages): Result<String> = try {
        Result.build { fireStoreSource.create(messages, Collections.MESSAGES) }
    } catch (e: Exception) {
        Result.build { throw Exception(e.message.toString()) }
    }

    suspend fun createComment(comments: Comments): Result<String> = try {
        Result.build { fireStoreSource.create(comments, Collections.COMMENTS) }
    } catch (e: Exception) {
        Result.build { throw Exception(e.message.toString()) }
    }

    @ExperimentalCoroutinesApi
    suspend fun fetchPublicMessages(): Flow<Messages> = callbackFlow {
        val subscription = fireStoreSource.fetchPublicMessages()
            .addSnapshotListener { value, error ->
                if (error == null) {
                    for (doc in value!!.documentChanges) {
                        offer(doc.document.toObject(Messages::class.java))
                    }
                }
            }

        awaitClose { subscription.remove() }
    }

    @ExperimentalCoroutinesApi
    suspend fun fetchComments(id: String): Flow<Comments> = callbackFlow {
        val subscription = fireStoreSource.fetchComments(id)
            .addSnapshotListener { value, error ->
                if (error == null) {
                    for (doc in value!!.documentChanges) {
                        offer(doc.document.toObject(Comments::class.java))
                    }
                }
            }

        awaitClose { subscription.remove() }
    }
}
