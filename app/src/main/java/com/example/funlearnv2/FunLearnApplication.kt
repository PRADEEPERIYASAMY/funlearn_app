package com.example.funlearnv2

import android.app.Application
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.google.GoogleEmojiProvider
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FunLearnApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        EmojiManager.install(GoogleEmojiProvider())
    }
}
