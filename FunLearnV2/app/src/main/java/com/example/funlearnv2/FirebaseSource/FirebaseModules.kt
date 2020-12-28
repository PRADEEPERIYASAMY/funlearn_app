package com.example.funlearnv2.FirebaseSource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object FirebaseModules {

    @Provides
    @Singleton
    fun giveFirebaseAuth():FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun giveFirebaseDatabase():FirebaseDatabase = FirebaseDatabase.getInstance()

    @Provides
    @Singleton
    fun givePhoneAuthProvider():PhoneAuthProvider = PhoneAuthProvider.getInstance()
}
