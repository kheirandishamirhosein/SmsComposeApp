package com.example.smscomposeapp.main

import android.app.Application
import com.example.smscomposeapp.di.SmsContainer

class SmsApp : Application() {

    override fun onCreate() {
        super.onCreate()
        SmsContainer.initialize(applicationContext)
    }

}