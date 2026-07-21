package com.example.funlearnv2.FirebaseSource

import com.example.funlearnv2.models.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class FireStoreSource @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
) {

    private val uid = firebaseAuth.uid.toString()

    // creating new docs

    suspend fun create(data: Any, collection: String): String = try {
        var result = ""
        firebaseFirestore.collection(collection).add(data)
            .addOnSuccessListener { result = "success" }
            .addOnFailureListener { result = it.message.toString() }
            .await()
        result
    } catch (e: Exception) {
        e.printStackTrace()
        "Error in uploading"
    }

    // incrementing or decreasing score and cash

    suspend fun updateCashOrScore(value: Long, field: String): String = try {
        var result = ""
        firebaseFirestore.collection(Collections.USERS).whereEqualTo("id", uid).get().addOnSuccessListener {
            it.documents[0].reference.update(field, FieldValue.increment(value))
            result = "updated"
        }
            .addOnFailureListener { result = it.message.toString() }
            .await()
        result
    } catch (e: Exception) {
        e.printStackTrace()
        "Error in uploading"
    }

    // fetch user details

    suspend fun getUserDetails(): Users = try {
        var users: Users? = null
        firebaseFirestore.collection(Collections.USERS).whereEqualTo("id", uid)
            .limit(1)
            .get()
            .addOnSuccessListener {
                users = it.documents[0].toObject(Users::class.java)
            }
            .addOnFailureListener { throw Exception(it.message.toString()) }
            .await()
        users!!
    } catch (e: Exception) {
        e.printStackTrace()
        throw (Exception("Error in Fetching"))
    }

    // fetch user stats

    suspend fun getUserStats(): Stats = try {
        var stats: Stats? = null
        firebaseFirestore.collection(Collections.STATS).whereEqualTo("user_id", uid)
            .limit(1)
            .get()
            .addOnSuccessListener {
                stats = it.documents[0].toObject(Stats::class.java)
            }
            .addOnFailureListener { throw Exception(it.message.toString()) }
            .await()
        stats!!
    } catch (e: Exception) {
        e.printStackTrace()
        throw (Exception("Error in Fetching"))
    }

    // fetch public messages

    fun fetchPublicMessages(): Query = try {
        firebaseFirestore.collection(Collections.MESSAGES)
            .whereEqualTo("mode", Mode.PUBLIC.type)
            .orderBy("id")
    } catch (e: Exception) {
        throw (Exception("Error in Fetching"))
    }

    // fetch comments

    fun fetchComments(id: String): Query = try {
        firebaseFirestore.collection(Collections.COMMENTS)
            .whereEqualTo("message_id", id)
            .orderBy("id")
    } catch (e: Exception) {
        throw (Exception("Error in Fetching"))
    }

    // fetch group messages

    suspend fun fetchGroupMessages(groupId: String): ArrayList<Messages> = try {
        val messageList = arrayListOf<Messages>()
        firebaseFirestore.collection(Collections.MESSAGES)
            .whereEqualTo("mode", Mode.GROUP)
            .whereEqualTo("to", groupId)
            .get()
            .addOnSuccessListener {
                it.forEach { queryDocumentSnapshot ->
                    messageList.add(queryDocumentSnapshot.toObject(Messages::class.java))
                }
            }
            .addOnFailureListener { throw Exception(it.message.toString()) }
            .await()
        messageList
    } catch (e: Exception) {
        e.printStackTrace()
        throw (Exception("Error in Fetching"))
    }

    // fetch group details

    suspend fun fetchGroupDetails(groupId: String): GroupDetails = try {
        var groupDetails: GroupDetails? = null
        firebaseFirestore.collection(Collections.GROUP_DETAILS)
            .whereEqualTo("mode", Mode.GROUP)
            .whereEqualTo("id", groupId)
            .limit(1)
            .get()
            .addOnSuccessListener {
                groupDetails = it.documents[0].toObject(GroupDetails::class.java)
            }
            .addOnFailureListener { throw Exception(it.message.toString()) }
            .await()
        groupDetails!!
    } catch (e: Exception) {
        e.printStackTrace()
        throw (Exception("Error in Fetching"))
    }

    // fetch group list

    suspend fun fetchGroupList(): ArrayList<GroupDetails> = try {
        val groupDetailsList = arrayListOf<GroupDetails>()
        firebaseFirestore.collection(Collections.GROUP_DETAILS)
            .whereEqualTo("mode", Mode.GROUP)
            .whereArrayContains("members", uid)
            .get()
            .addOnSuccessListener {
                it.forEach { queryDocumentSnapshot ->
                    groupDetailsList.add(queryDocumentSnapshot.toObject(GroupDetails::class.java))
                }
            }
            .addOnFailureListener { throw Exception(it.message.toString()) }
            .await()
        groupDetailsList
    } catch (e: Exception) {
        e.printStackTrace()
        throw (Exception("Error in Fetching"))
    }

    // fetch private messages

    suspend fun fetchPrivateMessages(groupId: String): ArrayList<Messages> = try {
        val messageList = arrayListOf<Messages>()
        firebaseFirestore.collection(Collections.MESSAGES)
            .whereEqualTo("mode", Mode.PRIVATE)
            .whereEqualTo("to", groupId)
            .get()
            .addOnSuccessListener {
                it.forEach { queryDocumentSnapshot ->
                    messageList.add(queryDocumentSnapshot.toObject(Messages::class.java))
                }
            }
            .addOnFailureListener { throw Exception(it.message.toString()) }
            .await()
        messageList
    } catch (e: Exception) {
        e.printStackTrace()
        throw (Exception("Error in Fetching"))
    }

    // fetch private list

    suspend fun fetchPrivateList(): ArrayList<GroupDetails> = try {
        val groupDetailsList = arrayListOf<GroupDetails>()
        firebaseFirestore.collection(Collections.GROUP_DETAILS)
            .whereEqualTo("mode", Mode.PRIVATE)
            .whereArrayContains("members", uid)
            .get()
            .addOnSuccessListener {
                it.forEach { queryDocumentSnapshot ->
                    groupDetailsList.add(queryDocumentSnapshot.toObject(GroupDetails::class.java))
                }
            }
            .addOnFailureListener { throw Exception(it.message.toString()) }
            .await()
        groupDetailsList
    } catch (e: Exception) {
        e.printStackTrace()
        throw (Exception("Error in Fetching"))
    }

    // fetch product list

    suspend fun fetchProductList(): ArrayList<Products> = try {
        val productList = arrayListOf<Products>()
        firebaseFirestore.collection(Collections.PRODUCTS)
            .orderBy("price")
            .get()
            .addOnSuccessListener {
                it.forEach { queryDocumentSnapshot ->
                    productList.add(queryDocumentSnapshot.toObject(Products::class.java))
                }
            }
            .addOnFailureListener { throw Exception(it.message.toString()) }
            .await()
        productList
    } catch (e: Exception) {
        e.printStackTrace()
        throw (Exception("Error in Fetching"))
    }

    // fetch stats list

    suspend fun fetchStatsList(): ArrayList<Stats> = try {
        val statsList = arrayListOf<Stats>()
        firebaseFirestore.collection(Collections.STATS)
            .whereEqualTo("user_id", uid)
            .get()
            .addOnSuccessListener {
                it.forEach { queryDocumentSnapshot ->
                    statsList.add(queryDocumentSnapshot.toObject(Stats::class.java))
                }
            }
            .addOnFailureListener { throw Exception(it.message.toString()) }
            .await()
        statsList
    } catch (e: Exception) {
        e.printStackTrace()
        throw (Exception("Error in Fetching"))
    }

    // fetch classRoom list

    suspend fun fetchClassRoomList(): ArrayList<ClassRoom> = try {
        val classRoomList = arrayListOf<ClassRoom>()
        firebaseFirestore.collection(Collections.CLASSROOM)
            .get()
            .addOnSuccessListener {
                it.forEach { queryDocumentSnapshot ->
                    classRoomList.add(queryDocumentSnapshot.toObject(ClassRoom::class.java))
                }
            }
            .addOnFailureListener { throw Exception(it.message.toString()) }
            .await()
        classRoomList
    } catch (e: Exception) {
        e.printStackTrace()
        throw (Exception("Error in Fetching"))
    }

    // fetch classResources list

    suspend fun fetchClassResourcesList(classId: String, resource: Resource): ArrayList<ClassResource> = try {
        val classResourceList = arrayListOf<ClassResource>()
        firebaseFirestore.collection(Collections.CLASS_RESOURCE)
            .whereEqualTo("class_id", classId)
            .whereEqualTo("type", resource)
            .get()
            .addOnSuccessListener {
                it.forEach { queryDocumentSnapshot ->
                    classResourceList.add(queryDocumentSnapshot.toObject(ClassResource::class.java))
                }
            }
            .addOnFailureListener { throw Exception(it.message.toString()) }
            .await()
        classResourceList
    } catch (e: Exception) {
        e.printStackTrace()
        throw (Exception("Error in Fetching"))
    }

    // fetch questions list

    suspend fun fetchQuestionsList(resourceId: String): ArrayList<Questions> = try {
        val questionsList = arrayListOf<Questions>()
        firebaseFirestore.collection(Collections.QUESTIONS)
            .whereEqualTo("resource_id", resourceId)
            .get()
            .addOnSuccessListener {
                it.forEach { queryDocumentSnapshot ->
                    questionsList.add(queryDocumentSnapshot.toObject(Questions::class.java))
                }
            }
            .addOnFailureListener { throw Exception(it.message.toString()) }
            .await()
        questionsList
    } catch (e: Exception) {
        e.printStackTrace()
        throw (Exception("Error in Fetching"))
    }

    // fetch orders list

    suspend fun fetchOrdersList(): ArrayList<Orders> = try {
        val ordersList = arrayListOf<Orders>()
        firebaseFirestore.collection(Collections.ORDERS)
            .whereEqualTo("by_id", uid)
            .get()
            .addOnSuccessListener {
                it.forEach { queryDocumentSnapshot ->
                    ordersList.add(queryDocumentSnapshot.toObject(Orders::class.java))
                }
            }
            .addOnFailureListener { throw Exception(it.message.toString()) }
            .await()
        ordersList
    } catch (e: Exception) {
        e.printStackTrace()
        throw (Exception("Error in Fetching"))
    }

    // fetch dashboard list

    suspend fun fetchDashboardList(): ArrayList<Dashboard> = try {
        val dashboardList = arrayListOf<Dashboard>()
        firebaseFirestore.collection(Collections.DASHBOARD)
            .orderBy("score")
            .get()
            .addOnSuccessListener {
                it.forEach { queryDocumentSnapshot ->
                    dashboardList.add(queryDocumentSnapshot.toObject(Dashboard::class.java))
                }
            }
            .addOnFailureListener { throw Exception(it.message.toString()) }
            .await()
        dashboardList
    } catch (e: Exception) {
        e.printStackTrace()
        throw (Exception("Error in Fetching"))
    }

    // fetch notifications list

    suspend fun fetchNotificationList(): ArrayList<Notifications> = try {
        val notificationsList = arrayListOf<Notifications>()
        firebaseFirestore.collection(Collections.NOTIFICATIONS)
            .orderBy("timestamp")
            .get()
            .addOnSuccessListener {
                it.forEach { queryDocumentSnapshot ->
                    notificationsList.add(queryDocumentSnapshot.toObject(Notifications::class.java))
                }
            }
            .addOnFailureListener { throw Exception(it.message.toString()) }
            .await()
        notificationsList
    } catch (e: Exception) {
        e.printStackTrace()
        throw (Exception("Error in Fetching"))
    }

    // fetch quizResults list

    suspend fun fetchQuizResultsList(): ArrayList<QuizResult> = try {
        val quizResultList = arrayListOf<QuizResult>()
        firebaseFirestore.collection(Collections.QUIZ_RESULTS)
            .whereEqualTo("user_id", uid)
            .get()
            .addOnSuccessListener {
                it.forEach { queryDocumentSnapshot ->
                    quizResultList.add(queryDocumentSnapshot.toObject(QuizResult::class.java))
                }
            }
            .addOnFailureListener { throw Exception(it.message.toString()) }
            .await()
        quizResultList
    } catch (e: Exception) {
        e.printStackTrace()
        throw (Exception("Error in Fetching"))
    }

    // fetch quizResult

    suspend fun fetchQuizResults(quizId: String): QuizResult = try {
        var quizResult: QuizResult? = null
        firebaseFirestore.collection(Collections.QUIZ_RESULTS)
            .whereEqualTo("quiz_id", quizId)
            .whereEqualTo("user_id", uid)
            .limit(1)
            .get()
            .addOnSuccessListener {
                quizResult = it.documents[0].toObject(QuizResult::class.java)
            }
            .addOnFailureListener { throw Exception(it.message.toString()) }
            .await()
        quizResult!!
    } catch (e: Exception) {
        e.printStackTrace()
        throw (Exception("Error in Fetching"))
    }
}
