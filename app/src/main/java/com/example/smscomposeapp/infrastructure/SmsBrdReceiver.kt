package com.example.smscomposeapp.infrastructure

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.telephony.SmsMessage
import com.example.smscomposeapp.data.models.MessageType
import com.example.smscomposeapp.data.models.SmsModel

class SmsBrdReceiver(private val viewModel: SmsViewModel) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val bundle = intent.extras
            val pdus = bundle?.get("pdus") as Array<*>
            for (pdu in pdus) {
                val smsMessage = SmsMessage.createFromPdu(pdu as ByteArray)
                val phoneNumber = smsMessage.originatingAddress
                val messageText = smsMessage.messageBody
                val smsModel =
                    phoneNumber?.let {
                        SmsModel(
                            phoneNumber = it,
                            message = messageText,
                            messageType = MessageType.RECEIVED
                        )
                    }
                smsModel?.let { viewModel.receiveSms(it) }
            }
        }
    }

/*
    override fun receiveSms(smsModel: SmsModel) {
        Log.d(
            "khkhkh SmsReceiver",
            "Received SMS: phoneNumber: ${smsModel.phoneNumber}, message: ${smsModel.message}"
        )

    }

 */
}