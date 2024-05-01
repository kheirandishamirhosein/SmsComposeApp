package com.example.smscomposeapp.doman

import com.example.smscomposeapp.data.models.SmsModel

interface SmsSender {
    fun sendSms(smsModel: SmsModel)
}