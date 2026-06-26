package com.example.signtranslate

import android.app.Application
import com.example.signtranslate.data.appContext

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = this
    }
}