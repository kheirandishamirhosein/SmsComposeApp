package com.example.smscomposeapp.di

import android.content.Context
import androidx.room.Room
import com.example.smscomposeapp.data.dao.SmsUserDao
import com.example.smscomposeapp.data.db.SmsUserDatabase
import com.example.smscomposeapp.infrastructure.SmsViewModel

object SmsFactory {

    fun provideSmsUserDatabase(context: Context): SmsUserDatabase {
        return Room.databaseBuilder(
            context = context.applicationContext,
            klass = SmsUserDatabase::class.java,
            name = "smsmodel.db"
        ).build()
    }

    fun provideSmsViewModel(dao: SmsUserDao): SmsViewModel {
        return SmsViewModel(dao)
    }
}