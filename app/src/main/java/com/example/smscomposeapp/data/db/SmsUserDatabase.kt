package com.example.smscomposeapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.smscomposeapp.data.dao.SmsUserDao
import com.example.smscomposeapp.data.models.SmsModel

@Database(entities = [SmsModel::class], version = 1)
abstract class SmsUserDatabase: RoomDatabase() {
    abstract val dao: SmsUserDao
}