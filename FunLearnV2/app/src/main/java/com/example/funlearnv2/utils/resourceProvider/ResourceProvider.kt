package com.example.funlearnv2.utils.resourceProvider

import android.content.Context
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore

class ResourceProvider(private val context: Context) {

    val userDataStore = context.createDataStore(name = "userData")

    object UserPreferenceKey {
        val id = preferencesKey<String>("id")
        val child_grade = preferencesKey<String>("child_grade")
        val child_name = preferencesKey<String>("child_name")
        val child_profile_image = preferencesKey<String>("child_profile_image")
        val child_online_visibility = preferencesKey<String>("child_online_visibility")
        val child_online = preferencesKey<String>("child_online")
        val child_mail = preferencesKey<String>("child_mail")
        val child_dob = preferencesKey<String>("child_dob")
        val child_gender = preferencesKey<String>("child_gender")
        val child_description = preferencesKey<String>("child_description")
        val parent_name = preferencesKey<String>("parent_name")
        val parent_dob = preferencesKey<String>("parent_dob")
        val parent_gender = preferencesKey<String>("parent_gender")
        val parent_grade = preferencesKey<String>("parent_grade")
        val parent_online_visibility = preferencesKey<String>("parent_online_visibility")
        val parent_online = preferencesKey<String>("parent_online")
        val parent_profile_image = preferencesKey<String>("parent_profile_image")
        val parent_mail = preferencesKey<String>("parent_mail")
        val account_password = preferencesKey<String>("account_password")
        val account_mail = preferencesKey<String>("account_mail")
        val mobile_number = preferencesKey<String>("mobile_number")
        val address = preferencesKey<String>("address")
        val score = preferencesKey<String>("score")
        val cash = preferencesKey<String>("cash")
    }
}
