package com.example.smscomposeapp.main

import android.app.Application
import com.example.smscomposeapp.di.SmsContainer
import com.example.smscomposeapp.util.AppSharePerf

class SmsApp : Application() {

    lateinit var sharePerf: AppSharePerf

    override fun onCreate() {
        super.onCreate()
        sharePerf = AppSharePerf(this)

        SmsContainer.initialize(applicationContext)
    }

}