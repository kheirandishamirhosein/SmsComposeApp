package com.example.smscomposeapp.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.smscomposeapp.data.models.SmsModel
import kotlinx.coroutines.flow.Flow

@Dao
interface SmsUserDao {

    /*
    @Upsert
    suspend fun upsertSmsUserModel(smsModel: SmsModel)

    @Query("SELECT * FROM smsmodel")
    fun getAllSmsRecord(): Flow<List<SmsModel>>
    */
}