package com.example.smscomposeapp.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FormatTimestamp {

    fun formatTimestamp(timestamp: Long): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

}