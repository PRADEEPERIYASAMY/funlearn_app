package com.example.funlearnv2.models

import androidx.annotation.Keep
import com.google.firebase.database.IgnoreExtraProperties

@Keep
@IgnoreExtraProperties
data class UserProfile(
    var Names: String? = null,
    var UserImages: String? = null,
    var DoB: String? = null,
    var StatusDescription: String? = null,
    var MiniDescription: String? = null,
    var Grade: String? = null,
    var Gender: String? = null,
    var DateOfJoining: String? = null,
    var Address: String? = null,
    var UserCoordinate: Coordinate? = null,
    var UserAge: String? = null,
    var Monitor: Monitor? = null,
)

@Keep
@IgnoreExtraProperties
data class Monitor(
    var MonitorName: String? = null,
    var MonitorType: String? = null,
    var MonitorPassword: String? = null,
    var MonitorImages: String? = null,
    var MonitorDoB: String? = null,
    var MonitorStatusDescription: String? = null,
    var MonitorMiniDescription: String? = null,
    var MonitorAge: String? = null,
)

@Keep
@IgnoreExtraProperties
data class Coordinate(
    var Latitude: Long? = null,
    var Longitude: Long? = null
)

@Keep
@IgnoreExtraProperties
data class PrivateChat(
    var Images: ArrayList<String>? = null,
    var Names: ArrayList<String>? = null,
)

@Keep
@IgnoreExtraProperties
data class GroupChat(
    var Images: ArrayList<String>? = null,
    var Names: ArrayList<String>? = null,
)

// common for all chat

@Keep
@IgnoreExtraProperties
data class Message(
    var MessageContent: String? = null,
    var MessageDescription: String? = null,
    var Type: String? = null,
    var Downloaded: String? = null,
    var Time: String? = null,
    var PostBy: String? = null,
    var CommentsCount: String? = null,
    var LikesCount: String? = null,
    var DisLikesCount: String? = null,
    var Comments: ArrayList<Comments>? = null
)

/*@Keep
@IgnoreExtraProperties
data class Comments(
    var MessageContent: String? = null,
    var Time: String? = null,
    var PostBy: String? = null,
    var LikesCount: String? = null,
    var DisLikesCount: String? = null,
)*/
