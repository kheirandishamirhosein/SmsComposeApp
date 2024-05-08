package com.example.smscomposeapp.util

import android.content.Context

class AppSharePerf(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    fun savePhoneNumber(phoneNumber: String) {
        sharedPreferences.edit().putString("phoneNumber", phoneNumber).apply()
    }

    fun getPhoneNumber(): String? {
        return sharedPreferences.getString("phoneNumber", null)
    }

}