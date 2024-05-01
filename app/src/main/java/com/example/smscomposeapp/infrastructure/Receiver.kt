package com.example.smscomposeapp.infrastructure

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.provider.Telephony
import androidx.annotation.RequiresApi
import com.example.smscomposeapp.data.models.SmsModel
import com.example.smscomposeapp.doman.SmsReceiver

object Receiver {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun receiveMessage(context: Context, callback: SmsReceiver) {
        val br = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
                for (sms in messages) {
                    val sender = sms.originatingAddress ?: ""
                    val message = sms.displayMessageBody ?: ""
                    val smsModel = SmsModel(sender, message)
                    callback.receiveSms(smsModel)
                }
            }
        }
        context.registerReceiver(
            br, IntentFilter("android.provider.Telephony.SMS_RECEIVED"),
            Context.RECEIVER_NOT_EXPORTED
        )
    }
}