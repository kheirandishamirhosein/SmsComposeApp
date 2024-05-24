package com.example.smscomposeapp.di

import android.content.Context
import androidx.room.Room
import com.example.smscomposeapp.data.dao.SmsUserDao
import com.example.smscomposeapp.data.db.SmsUserDatabase
import com.example.smscomposeapp.data.db.SmsUserDatabase.Companion.MIGRATION_1_2
import com.example.smscomposeapp.data.imp.ImpSmsSendRepository
import com.example.smscomposeapp.doman.SmsSender
import com.example.smscomposeapp.infrastructure.SmsViewModel

object SmsFactory {

    fun provideSmsUserDatabase(context: Context): SmsUserDatabase {
        return Room.databaseBuilder(
            context = context.applicationContext,
            klass = SmsUserDatabase::class.java,
            name = "smsmodel.db"
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    fun provideSmsSender(): SmsSender {
        return ImpSmsSendRepository()
    }

    fun provideSmsViewModel(dao: SmsUserDao, smsSender: SmsSender): SmsViewModel {
        return SmsViewModel(dao, smsSender)
    }

}