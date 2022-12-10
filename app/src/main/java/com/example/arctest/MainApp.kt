package com.example.arctest

import android.app.Application

class MainApp: Application() {

    companion object{
        lateinit var instance: Application
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}