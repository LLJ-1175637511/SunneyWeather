package com.example.sunnywhether

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class SWApplication:Application() {
    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context:Context
        const val TOKEN = "dOq70DAa2zZj5LIY"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}