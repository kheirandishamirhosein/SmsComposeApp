package com.example.smscomposeapp.util

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.smscomposeapp.main.MainActivity

class SmsPermission(private val context: FragmentActivity) {

    fun requestReceiveSmsPermission(requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECEIVE_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as FragmentActivity,
                arrayOf(Manifest.permission.RECEIVE_SMS),
                requestCode
            )
        }
    }

    fun requestSendSmsPermission(requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.SEND_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as FragmentActivity,
                arrayOf(Manifest.permission.SEND_SMS),
                requestCode
            )
        }
    }

    fun isReceiveSmsPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECEIVE_SMS
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun isSendSmsPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.SEND_SMS
        ) == PackageManager.PERMISSION_GRANTED
    }

}