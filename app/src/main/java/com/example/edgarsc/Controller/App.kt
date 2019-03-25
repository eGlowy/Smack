package com.example.edgarsc.Controller

import android.app.Application
import com.example.edgarsc.Utilities.SharedPrefts

class App: Application() {

    companion object {
        lateinit var prefs: SharedPrefts
    }

    override fun onCreate() {
        prefs = SharedPrefts(applicationContext)
        super.onCreate()
    }
}