package com.example.funlearnv2.utils.resourceProvider

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object ResourceProviderModule {

    @Provides
    @Singleton
    fun getResourceProvider(@ApplicationContext context: Context): ResourceProvider = ResourceProvider(context)
}
