package com.example.funlearnv2.repository.models

import androidx.annotation.Keep
import com.google.firebase.database.IgnoreExtraProperties

@Keep
@IgnoreExtraProperties
data class DataModel(
        var name:String?=null,
        var path:String?=null,
        var clicked:Boolean?=null,
        var items:String?=null,
        var date:String?=null,
        var type:String?=null
)
