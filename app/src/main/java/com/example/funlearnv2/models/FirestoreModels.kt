package com.example.funlearnv2.models

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@Keep
@IgnoreExtraProperties
data class Users(
    var id: String? = "", // uid
    var child_grade: String = Grade.PREKG.type,
    var child_name: String? = "",
    var child_profile_image: String? = "",
    var child_online_visibility: String = Choice.NO.type,
    var child_online: String = OnlineVisibility.ONLINE.type,
    var child_mail: String? = "",
    var child_dob: String? = "",
    var child_gender: String? = "",
    var child_description: String? = "",
    var parent_name: String? = "",
    var parent_dob: String? = "",
    var parent_gender: String? = "",
    var parent_grade: String = ParentGrade.COLLEGE.type,
    var parent_online_visibility: String = Choice.NO.type,
    var parent_online: String = OnlineVisibility.ONLINE.type,
    var parent_profile_image: String? = "",
    var parent_mail: String? = "",
    var account_password: String? = null,
    var account_mail: String? = null,
    var mobile_number: String? = "",
    var address: String? = "",
    var geoPoint: GeoPoint? = null,
    var timestamp: FieldValue? = null,
    var child_lastSeen: FieldValue? = null,
    var parent_lastSeen: FieldValue? = null,
    var score: Long? = 0,
    var cash: Long? = 0,
)

@Keep
@IgnoreExtraProperties
data class Stats(
    var user_id: String? = "",
    var score: ArrayList<String>? = null, // date------score
    var cash: ArrayList<String>? = null, // date------cash
)

@Keep
@IgnoreExtraProperties
data class Messages(
    var id: String? = "", // uid------current_time_in _ms
    var role: String? = Roles.CHILD.type,
    var mode: String? = Mode.PUBLIC.type,
    var from: String? = "", // uid
    var to: String? = "", // uid
    var message_content: String? = "",
    var message_description: String? = "",
    @ServerTimestamp
    var timeStamp: Timestamp? = null,
    var message_type: String? = MessageTypes.TEXT.type,
    var deleted_by: ArrayList<String>? = arrayListOf(), // uid------role
)

@Keep
@IgnoreExtraProperties
data class Reactions(
    var message_id: String? = "",
    var user_id: String? = "",
    var reaction: String? = "",
)

@Keep
@IgnoreExtraProperties
data class Comments(
    var id: String? = "", // uid------current_time_in _ms
    var message_id: String? = "",
    var from: String? = "",
    var from_name: String? = "",
    var comment: String? = "",
    var timeStamp: Timestamp? = null,
)

@Keep
@IgnoreExtraProperties
data class QuizResult(
    var quiz_id: String? = "",
    var user_id: String? = "",
    var score: String? = "",
    var attempted: String? = "",
    var un_attempted: String? = "",
    var wrong: String? = "",
    var timeStamp: FieldValue? = null,
)

@Keep
@IgnoreExtraProperties
data class Dashboard(
    var user_id: String? = "",
    var score: String? = "",
    var name: String? = "",
    var image: String? = "",
)

@Keep
@IgnoreExtraProperties
data class ClassResource(
    var id: String? = "", // uid------current_time_in _ms
    var class_id: String? = "",
    var name: String? = "",
    var image: String? = "",
    var url: String? = "",
    var duration: String? = "",
    var pages: String? = "",
    var author_name: String? = "",
    var author_id: String? = "",
    var type: Resource? = Resource.LINK,
    var timeStamp: FieldValue? = null,
)

@Keep
@IgnoreExtraProperties
data class ClassRoom(
    var id: String? = "", // uid------current_time_in _ms
    var name: String? = "",
    var image: String? = "",
    var meet_url: String? = "",
    var teacher_name: String? = "",
    var teacher_id: String? = "",
    var description: String? = "",
    var timeStamp: String? = "",
)

@Keep
@IgnoreExtraProperties
data class Questions(
    var resource_id: String? = "",
    var question: String? = "",
    var option_A: String? = "",
    var option_b: String? = "",
    var option_c: String? = "",
    var option_D: String? = "",
    var Answer: String? = "",

)

@Keep
@IgnoreExtraProperties
data class Notifications(
    var message_description: String? = "",
    var timeStamp: FieldValue? = null,
    var mode: String? = "",
    var to_id: String? = "",
    var deleted_by: ArrayList<String>? = null, // uid
)

@Keep
@IgnoreExtraProperties
data class Queries(
    var message_description: String? = "",
    var from_id: String? = "",
    var from_name: String? = "",
    var timeStamp: FieldValue? = null,
)

@Keep
@IgnoreExtraProperties
data class Requests(
    var from_id: String? = "",
    var from_name: String? = "",
    var to_id: String? = "",
    var to_name: String? = "",
    var status: Status? = Status.PENDING,
    var timeStamp: FieldValue? = null,
)

@Keep
@IgnoreExtraProperties
data class GroupDetails(
    var id: String? = "", // uid------current_time_in _ms
    var group_name: String? = "",
    var group_description: String? = "",
    var mode: Mode? = Mode.GROUP,
    var timeStamp: FieldValue? = null,
    var members: ArrayList<String>? = null, // should be uid
)

@Keep
@IgnoreExtraProperties
data class Products(
    var id: String? = "", // number
    var name: String? = "",
    var price: String? = "",
    var description: String? = "",
    var images: String? = "",
)

@Keep
@IgnoreExtraProperties
data class Orders(
    var by_id: String? = "", // uid
    var by_name: String? = "",
    var name: String? = "",
    var address: String? = "",
    var description: String? = "",
    var images: String? = "",
    var status: OrderStatus? = OrderStatus.PENDING,
    var product_id: String? = "",
)

enum class Roles(val type: String) {
    TEACHER("teacher"),
    CHILD("child"),
    PARENT("parent"),
    ADMIN("admin"),
}

enum class Gender(val type: String) {
    MALE("male"),
    FEMALE("female"),
    NOTOSAY("not to say");
    override fun toString(): String = type
}

enum class Grade(val type: String) {
    PREKG("pre kg"),
    LKG("lkg"),
    UKG("ukg");
    override fun toString(): String = type
}

enum class ParentGrade(val type: String) {
    SCHOOL("school"),
    COLLEGE("college"),
    NOCOMMENTS("no comments");
    override fun toString(): String = type
}

enum class Choice(val type: String) {
    YES("yes"),
    NO("no");
    override fun toString(): String = type
}

enum class Mode(val type: String) {
    PRIVATE("private"),
    PUBLIC("public"),
    GROUP("group"),
}

enum class Status(val type: String) {
    REJECTED("rejected"),
    PENDING("pending"),
}

enum class OrderStatus(val type: String) {
    DELIVERED("delivered"),
    PENDING("pending"),
}

enum class Resource(val type: String) {
    LINK("link"),
    PDF("pdf"),
    VIDEO("video"),
    QUIZ("quiz"),
}

enum class OnlineVisibility(val type: String) {
    ONLINE("online"),
    OFFLINE("offline"),
}

enum class MessageTypes(val type: String) {
    IMAGE("image"),
    PDF("pdf"),
    TEXT("text"),
    VIDEO("video"),
}
