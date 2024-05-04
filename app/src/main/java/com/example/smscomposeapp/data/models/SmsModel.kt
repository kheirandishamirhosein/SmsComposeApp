package com.example.smscomposeapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SmsModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val phoneNumber: String,
    val message: String
)
