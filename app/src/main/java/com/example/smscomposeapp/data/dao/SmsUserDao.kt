package com.example.smscomposeapp.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.smscomposeapp.data.models.SmsModel
import kotlinx.coroutines.flow.Flow

@Dao
interface SmsUserDao {

    @Upsert
    suspend fun upsertSmsUserModel(smsModel: SmsModel)

    @Query("SELECT * FROM smsmodel")
    fun getAllSmsRecord(): Flow<List<SmsModel>>

    //TODO: Add for Message
    @Query("SELECT * FROM SmsModel ORDER BY id ASC")
    fun getAllSmsMessages(): Flow<List<SmsModel>>

    /*
        @Query(
        "SELECT * FROM SmsModel " +
                "WHERE phoneNumber IN " +
                "(SELECT phoneNumber From SmsModel GROUP BY phoneNumber ) " +
                "ORDER BY id DESC"
    )
     */
    @Query(
        """
    SELECT * FROM SmsModel 
    WHERE id IN (
        SELECT MAX(id) 
        FROM SmsModel 
        GROUP BY phoneNumber
    ) 
    ORDER BY id DESC
"""
    )
    fun getLastSmsMessage(): Flow<List<SmsModel>>

    //get messages by phone number
    @Query("SELECT * FROM SmsModel WHERE phoneNumber = :phoneNumber ORDER BY id ASC")
    fun getSmsByPhoneNumber(phoneNumber: String): Flow<List<SmsModel>>

}