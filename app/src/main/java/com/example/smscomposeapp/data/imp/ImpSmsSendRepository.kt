package com.example.smscomposeapp.data.imp

import android.telephony.SmsManager
import com.example.smscomposeapp.data.models.SmsModel
import com.example.smscomposeapp.doman.SmsSender

class ImpSmsSendRepository() : SmsSender {
    override fun sendSms(smsModel: SmsModel) {
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(smsModel.phoneNumber, null, smsModel.message, null, null)
    }
}