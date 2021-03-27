package com.example.funlearnv2.repository

import androidx.datastore.preferences.core.edit
import com.example.funlearnv2.utils.resourceProvider.ResourceProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class DataStoreRepository @Inject constructor(
    private val resourceProvider: ResourceProvider
) {

    // flow
    private val getIdFlow: Flow<String> = resourceProvider.userDataStore.data.catch {
        if (it is IOException) it.printStackTrace()
        else throw it
    }.map { preferences -> preferences[ResourceProvider.UserPreferenceKey.id] ?: "" }

    private val getChildGradeFlow: Flow<String> = resourceProvider.userDataStore.data.catch {
        if (it is IOException) it.printStackTrace()
        else throw it
    }.map { preferences -> preferences[ResourceProvider.UserPreferenceKey.child_grade] ?: "" }

    private val getChildNameFlow: Flow<String> = resourceProvider.userDataStore.data.catch {
        if (it is IOException) it.printStackTrace()
        else throw it
    }.map { preferences -> preferences[ResourceProvider.UserPreferenceKey.child_name] ?: "" }

    private val getChildProfileImageFlow: Flow<String> = resourceProvider.userDataStore.data.catch {
        if (it is IOException) it.printStackTrace()
        else throw it
    }.map { preferences -> preferences[ResourceProvider.UserPreferenceKey.child_profile_image] ?: "" }

    private val getChildOnlineVisibilityFlow: Flow<String> = resourceProvider.userDataStore.data.catch {
        if (it is IOException) it.printStackTrace()
        else throw it
    }.map { preferences -> preferences[ResourceProvider.UserPreferenceKey.child_online_visibility] ?: "" }

    private val getChildOnlineFlow: Flow<String> = resourceProvider.userDataStore.data.catch {
        if (it is IOException) it.printStackTrace()
        else throw it
    }.map { preferences -> preferences[ResourceProvider.UserPreferenceKey.child_online] ?: "" }

    private val getChildMailFlow: Flow<String> = resourceProvider.userDataStore.data.catch {
        if (it is IOException) it.printStackTrace()
        else throw it
    }.map { preferences -> preferences[ResourceProvider.UserPreferenceKey.child_mail] ?: "" }

    private val getChildDobFlow: Flow<String> = resourceProvider.userDataStore.data.catch {
        if (it is IOException) it.printStackTrace()
        else throw it
    }.map { preferences -> preferences[ResourceProvider.UserPreferenceKey.child_dob] ?: "" }

    private val getChildGenderFlow: Flow<String> = resourceProvider.userDataStore.data.catch {
        if (it is IOException) it.printStackTrace()
        else throw it
    }.map { preferences -> preferences[ResourceProvider.UserPreferenceKey.child_gender] ?: "" }

    private val getChildDescriptionFlow: Flow<String> = resourceProvider.userDataStore.data.catch {
        if (it is IOException) it.printStackTrace()
        else throw it
    }.map { preferences -> preferences[ResourceProvider.UserPreferenceKey.child_description] ?: "" }

    private val getParentNameFlow: Flow<String> = resourceProvider.userDataStore.data.catch {
        if (it is IOException) it.printStackTrace()
        else throw it
    }.map { preferences -> preferences[ResourceProvider.UserPreferenceKey.parent_name] ?: "" }

    private val getParentDobFlow: Flow<String> = resourceProvider.userDataStore.data.catch {
        if (it is IOException) it.printStackTrace()
        else throw it
    }.map { preferences -> preferences[ResourceProvider.UserPreferenceKey.parent_dob] ?: "" }

    private val getParentGenderFlow: Flow<String> = resourceProvider.userDataStore.data.catch {
        if (it is IOException) it.printStackTrace()
        else throw it
    }.map { preferences -> preferences[ResourceProvider.UserPreferenceKey.parent_gender] ?: "" }

    private val getParentGradeFlow: Flow<String> = resourceProvider.userDataStore.data.catch {
        if (it is IOException) it.printStackTrace()
        else throw it
    }.map { preferences -> preferences[ResourceProvider.UserPreferenceKey.parent_grade] ?: "" }

    private val getParentOnlineVisibilityFlow: Flow<String> = resourceProvider.userDataStore.data.catch {
        if (it is IOException) it.printStackTrace()
        else throw it
    }.map { preferences -> preferences[ResourceProvider.UserPreferenceKey.parent_online_visibility] ?: "" }

    private val getParentOnlineFlow: Flow<String> = resourceProvider.userDataStore.data.catch {
        if (it is IOException) it.printStackTrace()
        else throw it
    }.map { preferences -> preferences[ResourceProvider.UserPreferenceKey.parent_online] ?: "" }

    private val getParentProfileImageFlow: Flow<String> = resourceProvider.userDataStore.data.catch {
        if (it is IOException) it.printStackTrace()
        else throw it
    }.map { preferences -> preferences[ResourceProvider.UserPreferenceKey.parent_profile_image] ?: "" }

    private val getParentMailFlow: Flow<String> = resourceProvider.userDataStore.data.catch {
        if (it is IOException) it.printStackTrace()
        else throw it
    }.map { preferences -> preferences[ResourceProvider.UserPreferenceKey.parent_mail] ?: "" }

    private val getAccountPasswordFlow: Flow<String> = resourceProvider.userDataStore.data.catch {
        if (it is IOException) it.printStackTrace()
        else throw it
    }.map { preferences -> preferences[ResourceProvider.UserPreferenceKey.account_password] ?: "" }

    private val getAccountMailFlow: Flow<String> = resourceProvider.userDataStore.data.catch {
        if (it is IOException) it.printStackTrace()
        else throw it
    }.map { preferences -> preferences[ResourceProvider.UserPreferenceKey.account_mail] ?: "" }

    private val getMobileNumberFlow: Flow<String> = resourceProvider.userDataStore.data.catch {
        if (it is IOException) it.printStackTrace()
        else throw it
    }.map { preferences -> preferences[ResourceProvider.UserPreferenceKey.mobile_number] ?: "" }

    private val getAddressFlow: Flow<String> = resourceProvider.userDataStore.data.catch {
        if (it is IOException) it.printStackTrace()
        else throw it
    }.map { preferences -> preferences[ResourceProvider.UserPreferenceKey.address] ?: "" }

    private val getScoreFlow: Flow<String> = resourceProvider.userDataStore.data.catch {
        if (it is IOException) it.printStackTrace()
        else throw it
    }.map { preferences -> preferences[ResourceProvider.UserPreferenceKey.score] ?: "" }

    private val getCashFlow: Flow<String> = resourceProvider.userDataStore.data.catch {
        if (it is IOException) it.printStackTrace()
        else throw it
    }.map { preferences -> preferences[ResourceProvider.UserPreferenceKey.cash] ?: "" }

    // store
    suspend fun storeId(id: String) = resourceProvider.userDataStore.edit { it[ResourceProvider.UserPreferenceKey.id] = id }
    suspend fun storeChildGrade(childGrade: String) = resourceProvider.userDataStore.edit { it[ResourceProvider.UserPreferenceKey.child_grade] = childGrade }
    suspend fun storeChildName(childName: String) = resourceProvider.userDataStore.edit { it[ResourceProvider.UserPreferenceKey.child_name] = childName }
    suspend fun storeChildProfileImage(childProfileImage: String) = resourceProvider.userDataStore.edit { it[ResourceProvider.UserPreferenceKey.child_profile_image] = childProfileImage }
    suspend fun storeChildOnlineVisibility(childOnlineVisibility: String) = resourceProvider.userDataStore.edit { it[ResourceProvider.UserPreferenceKey.child_online_visibility] = childOnlineVisibility }
    suspend fun storeChildOnline(childOnline: String) = resourceProvider.userDataStore.edit { it[ResourceProvider.UserPreferenceKey.child_online] = childOnline }
    suspend fun storeChildMail(childMail: String) = resourceProvider.userDataStore.edit { it[ResourceProvider.UserPreferenceKey.child_mail] = childMail }
    suspend fun storeChildDob(childDob: String) = resourceProvider.userDataStore.edit { it[ResourceProvider.UserPreferenceKey.child_dob] = childDob }
    suspend fun storeChildGender(childGender: String) = resourceProvider.userDataStore.edit { it[ResourceProvider.UserPreferenceKey.child_gender] = childGender }
    suspend fun storeChildDescription(childDescription: String) = resourceProvider.userDataStore.edit { it[ResourceProvider.UserPreferenceKey.child_description] = childDescription }
    suspend fun storeParentName(parentName: String) = resourceProvider.userDataStore.edit { it[ResourceProvider.UserPreferenceKey.parent_name] = parentName }
    suspend fun storeParentDob(parentDob: String) = resourceProvider.userDataStore.edit { it[ResourceProvider.UserPreferenceKey.parent_dob] = parentDob }
    suspend fun storeParentGender(parentGender: String) = resourceProvider.userDataStore.edit { it[ResourceProvider.UserPreferenceKey.parent_gender] = parentGender }
    suspend fun storeParentGrade(parentGrade: String) = resourceProvider.userDataStore.edit { it[ResourceProvider.UserPreferenceKey.parent_grade] = parentGrade }
    suspend fun storeParentOnlineVisibility(parentOnlineVisibility: String) = resourceProvider.userDataStore.edit { it[ResourceProvider.UserPreferenceKey.parent_online_visibility] = parentOnlineVisibility }
    suspend fun storeParentOnline(parentOnline: String) = resourceProvider.userDataStore.edit { it[ResourceProvider.UserPreferenceKey.parent_online] = parentOnline }
    suspend fun storeParentProfileImage(parentProfileImage: String) = resourceProvider.userDataStore.edit { it[ResourceProvider.UserPreferenceKey.parent_profile_image] = parentProfileImage }
    suspend fun storeParentMail(parentMail: String) = resourceProvider.userDataStore.edit { it[ResourceProvider.UserPreferenceKey.parent_mail] = parentMail }
    suspend fun storeAccountPassword(accountPassword: String) = resourceProvider.userDataStore.edit { it[ResourceProvider.UserPreferenceKey.account_password] = accountPassword }
    suspend fun storeAccountMail(accountMail: String) = resourceProvider.userDataStore.edit { it[ResourceProvider.UserPreferenceKey.account_mail] = accountMail }
    suspend fun storeMobileNumber(mobileNumber: String) = resourceProvider.userDataStore.edit { it[ResourceProvider.UserPreferenceKey.mobile_number] = mobileNumber }
    suspend fun storeAddress(address: String) = resourceProvider.userDataStore.edit { it[ResourceProvider.UserPreferenceKey.address] = address }
    suspend fun storeScore(score: String) = resourceProvider.userDataStore.edit { it[ResourceProvider.UserPreferenceKey.score] = score }
    suspend fun storeCash(cash: String) = resourceProvider.userDataStore.edit { it[ResourceProvider.UserPreferenceKey.cash] = cash }

    // get
    suspend fun getId(): String = getIdFlow.first()
    suspend fun getChildGrade(): String = getChildGradeFlow.first()
    suspend fun getChildName(): String = getChildNameFlow.first()
    suspend fun getChildProfileImage(): String = getChildProfileImageFlow.first()
    suspend fun getChildOnlineVisibility(): String = getChildOnlineVisibilityFlow.first()
    suspend fun getChildOnline(): String = getChildOnlineFlow.first()
    suspend fun getChildMail(): String = getChildMailFlow.first()
    suspend fun getChildDob(): String = getChildDobFlow.first()
    suspend fun getChildGender(): String = getChildGenderFlow.first()
    suspend fun getChildDescription(): String = getChildDescriptionFlow.first()
    suspend fun getParentName(): String = getParentNameFlow.first()
    suspend fun getParentDob(): String = getParentDobFlow.first()
    suspend fun getParentGender(): String = getParentGenderFlow.first()
    suspend fun getParentGrade(): String = getParentGradeFlow.first()
    suspend fun getParentOnlineVisibility(): String = getParentOnlineVisibilityFlow.first()
    suspend fun getParentOnline(): String = getParentOnlineFlow.first()
    suspend fun getParentProfileImage(): String = getParentProfileImageFlow.first()
    suspend fun getParentMail(): String = getParentMailFlow.first()
    suspend fun getAccountPassword(): String = getAccountPasswordFlow.first()
    suspend fun getAccountMail(): String = getAccountMailFlow.first()
    suspend fun getMobileNumber(): String = getMobileNumberFlow.first()
    suspend fun getAddress(): String = getAddressFlow.first()
    suspend fun getScore(): String = getScoreFlow.first()
    suspend fun getCash(): String = getCashFlow.first()
}
