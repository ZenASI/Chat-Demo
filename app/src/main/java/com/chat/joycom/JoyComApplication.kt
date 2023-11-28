package com.chat.joycom

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class JoyComApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}