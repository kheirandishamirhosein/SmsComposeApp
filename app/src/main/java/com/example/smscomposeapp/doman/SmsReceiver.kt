package com.example.smscomposeapp.doman

import com.example.smscomposeapp.data.models.SmsModel

interface SmsReceiver {
    fun receiveSms(smsModel: SmsModel)
}