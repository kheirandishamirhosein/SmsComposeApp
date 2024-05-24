package com.example.smscomposeapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.smscomposeapp.data.dao.SmsUserDao
import com.example.smscomposeapp.data.models.SmsModel

@Database(entities = [SmsModel::class], version = 2, exportSchema = false)
abstract class SmsUserDatabase : RoomDatabase() {

    companion object {
        val MIGRATION_1_2 = Migration1To2()
    }
    abstract val dao: SmsUserDao
}