package com.example.smscomposeapp.di

import android.content.Context
import com.example.smscomposeapp.data.dao.SmsUserDao
import com.example.smscomposeapp.data.db.SmsUserDatabase
import com.example.smscomposeapp.infrastructure.SmsViewModel

object SmsContainer {
    private lateinit var smsDb: SmsUserDatabase
    private lateinit var smsUserDao: SmsUserDao
    private lateinit var smsViewModel: SmsViewModel

    fun initialize(context: Context) {
        smsDb = SmsFactory.provideSmsUserDatabase(context.applicationContext)
        smsUserDao = smsDb.dao
        smsViewModel = SmsFactory.provideSmsViewModel(smsUserDao)
    }

    fun getSmsViewModel(): SmsViewModel {
        return smsViewModel
    }

}